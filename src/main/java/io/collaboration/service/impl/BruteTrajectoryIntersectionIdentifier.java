package io.collaboration.service.impl;

import io.collaboration.api.EnhancedCoordinate;
import io.collaboration.api.Intersection;
import io.collaboration.helper.EuclideanDistance2D;
import io.collaboration.service.AbstractTrajectoryIntersectionIdentifier;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * This is a brute force approach on the minimum distance between two sets of points problem. The algorithm evaluates
 * the Euclidean distance between all planar points from the two provided UIDs and filters out the ones with Euclidean distance
 * greater than spatialEpsilon. The time complexity of the brute force approach is O(n^2) since the
 * {@link #identify(List, List, Double, Integer)} method has to traverse the entirety of both list of coordinates
 * (worst case scenario) in order to come up with the intersecting points.
 */
@Component
public class BruteTrajectoryIntersectionIdentifier extends AbstractTrajectoryIntersectionIdentifier {

    private static final Logger LOGGER = Logger.getLogger(BruteTrajectoryIntersectionIdentifier.class);

    @Override
    public Optional<Intersection> identify(List<EnhancedCoordinate> one,
                                           List<EnhancedCoordinate> two,
                                           Double spatialEpsilon,
                                           Integer temporalEpsilon) {

        if (!validate(one, two)) return Optional.empty();

        LOGGER.trace("Number of coordinates for first trajectory is " + one.size()
                + ". Number of coordinates for second trajectory is " + two.size());

        for (EnhancedCoordinate co : one) {
            Optional<EnhancedCoordinate> second = two.stream()
                    .filter(ct -> EuclideanDistance2D.getDistance(co.getX(),
                            co.getY(),
                            ct.getX(),
                            ct.getY()) <= spatialEpsilon &&
                            co.getFloor() == ct.getFloor() &&
                            Math.abs(ChronoUnit.SECONDS.between(co.getTimestamp(), ct.getTimestamp())) <= temporalEpsilon)
                    .findFirst();

            if (second.isPresent()) {
                return Optional.of(new Intersection(co, second.get()));
            }
        }

        return Optional.empty();
    }


    @Override
    public String getName() {
        return "brute";
    }
}
