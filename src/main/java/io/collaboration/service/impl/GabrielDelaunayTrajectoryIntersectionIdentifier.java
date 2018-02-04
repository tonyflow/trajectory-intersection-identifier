package io.collaboration.service.impl;

import com.google.common.collect.Sets;
import io.collaboration.api.EnhancedCoordinate;
import io.collaboration.api.Intersection;
import io.collaboration.service.AbstractTrajectoryIntersectionIdentifier;
import org.apache.log4j.Logger;
import org.kynosarges.tektosyne.geometry.*;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Computation is based on <a href="http://www-cgrl.cs.mcgill.ca/~godfried/publications/mindist.pdf"> Toussaint & Bhattacharya</a>'s
 * paper suggesting that the minimum distance between two sets of planar points can be
 * computed in O(nlogn) in the worst case. If S(1) and S(2) are the planar sets under examination then:
 * <ul>
 * <li>Compute the Voronoi diagram and its dual graph (the Delaunay triangulation) for S = S(1) U S(2)</li>
 * <li>Obtain the Gabriel graph of S which is a subgraph of the Delauny triangulation</li>
 * <li>Filter out all edges smaller than spatialEpsilon</li>
 * <li>From among the edges of the Gabriel graph, determine the set of pairs of points such that one is from S(1)
 * and the other from S(2)</li>
 * <li>Check that the interval between timestamps is smaller than temporalEpsilon</li>
 * <li>Check floor equality</li>
 * <p>
 * <p>
 * </ul>
 */
@Component
public class GabrielDelaunayTrajectoryIntersectionIdentifier extends AbstractTrajectoryIntersectionIdentifier {

    private static final Logger LOGGER = Logger.getLogger(GabrielDelaunayTrajectoryIntersectionIdentifier.class);

    @Override
    public Optional<Intersection> identify(List<EnhancedCoordinate> one,
                                           List<EnhancedCoordinate> two,
                                           Double spatialEpsilon,
                                           Integer temporalEpsilon) {

        if (!validate(one, two)) return Optional.empty();

        LOGGER.trace("Number of coordinates for first trajectory is " + one.size()
                + ". Number of coordinates for second trajectory is " + two.size());

        List<EnhancedCoordinate> all = new ArrayList<>(one);
        all.addAll(two);

        // Create Gabriel graph out of the two sets of planar points
        Set<LineD> gabrielGraph = createGabrielGraph(all);

        // Filter out all edges with a length larger that spatialEpsilon
        Optional<Intersection> filtered = gabrielGraph.stream().filter(e -> e.length() <= spatialEpsilon)
                // Filter out edges between points owned by first UID
                .filter(outOne -> !pointsShareUID(outOne.start, outOne.end, one))
                // Filter out edges between points owned by second UID
                .filter(outTwo -> !pointsShareUID(outTwo.start, outTwo.end, two))
                // Map to EnhancedCoordinate and group by floor
                .map(lele -> convertToPotentialIntersections(lele, all))
                // Check temporalEpsilon
                .filter(coco -> ChronoUnit.SECONDS.between(coco.getPointOne().getTimestamp(), coco.getPointTwo().getTimestamp()) <= temporalEpsilon)
                // Do not evaluate entire stream. Return first fitting criteria
                .findFirst();

        if (filtered.isPresent()) return filtered;

        return Optional.empty();
    }

    @Override
    public String getName() {
        return "gabriel";
    }

    /**
     * Create <a href="https://en.wikipedia.org/wiki/Gabriel_graph">Gabriel graph</a> from union
     * of two point sets.
     *
     * @param all All coordinates
     * @return Gabriel graph set of edges
     */
    public Set<LineD> createGabrielGraph(List<EnhancedCoordinate> all) {

        Set<LineD> gabrielGraph = Sets.newConcurrentHashSet();

        // Define a clipping rectangle for the Voronoi diagram
        RectD clip = new RectD(all.stream().min(Comparator.comparing(EnhancedCoordinate::getX)).get().getX(),
                all.stream().min(Comparator.comparing(EnhancedCoordinate::getY)).get().getY(),
                all.stream().max(Comparator.comparing(EnhancedCoordinate::getX)).get().getX(),
                all.stream().max(Comparator.comparing(EnhancedCoordinate::getY)).get().getY());

        PointD[] pointsDUnion = all.stream().map(co -> new PointD(co.getX(), co.getY())).toArray(PointD[]::new);
        VoronoiResults results = Voronoi.findAll(pointsDUnion, clip);
        LineD[] delaunayEdges = results.delaunayEdges();
        VoronoiEdge[] voronoiEdges = results.voronoiEdges;
        PointD[] voronoiVertices = results.voronoiVertices;

        for (int i = 0; i < delaunayEdges.length; i++) {

            // Find Delaunay's edge dual Voronoi edge
            LineD dualVoronoiEdge = new LineD(voronoiVertices[voronoiEdges[i].vertex1].x,
                    voronoiVertices[voronoiEdges[i].vertex2].x,
                    voronoiVertices[voronoiEdges[i].vertex1].y,
                    voronoiVertices[voronoiEdges[i].vertex2].y);

            // Determine whether or not the edges intersect
            LineIntersection intersection = delaunayEdges[i].intersect(dualVoronoiEdge);

            // If the relation between the edges is divergent
            if (intersection.relation == LineRelation.DIVERGENT) {
                // Then this edge is part of the Gabriel Graph
                // Adding this to the set of Gabriel Graph edges
                gabrielGraph.add(delaunayEdges[i]);
            }
        }

        return gabrielGraph;
    }

    /**
     * Determine whether the start and the end of the line segment which is part of the
     * Gabriel graph are part of the same UID's coordinates.
     *
     * @param start       Start of Gabriel graph edge
     * @param end         End of Gabriel graph edge
     * @param coordinates
     * @return true if both start and end of Gabriel graph edge belong to same UID
     */
    public boolean pointsShareUID(PointD start,
                                  PointD end,
                                  List<EnhancedCoordinate> coordinates) {

        boolean containsStart = coordinates.stream().filter(coo -> coo.equals(start)).count() > 0;
        boolean containsEnd = coordinates.stream().filter(coo -> coo.equals(end)).count() > 0;

        return containsStart && containsEnd;
    }

    /**
     * Convert {@link LineD} (used by the kynoserges library) back to {@link EnhancedCoordinate}
     *
     * @param segment Gabriel graph edge which is characterised by start and end owned by distinct UIDs.
     * @param all     Union of all coordinates
     * @return a potential {@link Intersection} point
     */
    private Intersection convertToPotentialIntersections(LineD segment,
                                                         List<EnhancedCoordinate> all) {
        Optional<EnhancedCoordinate> first = all.stream().filter(coo -> coo.equals(segment.start)).findFirst();
        Optional<EnhancedCoordinate> second = all.stream().filter(coo -> coo.equals(segment.end)).findFirst();
        return new Intersection(first.get(), second.get());
    }

}
