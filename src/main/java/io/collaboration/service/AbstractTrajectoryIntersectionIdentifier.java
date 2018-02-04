package io.collaboration.service;

import io.collaboration.api.EnhancedCoordinate;
import io.collaboration.api.Intersection;
import io.collaboration.factory.TrajectoryIntersectionIdentifierFactory;
import io.collaboration.service.impl.BruteTrajectoryIntersectionIdentifier;
import io.collaboration.service.impl.GabrielDelaunayTrajectoryIntersectionIdentifier;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * Specification of the identifiers ({@link GabrielDelaunayTrajectoryIntersectionIdentifier},
 * {@link BruteTrajectoryIntersectionIdentifier})
 */
public abstract class AbstractTrajectoryIntersectionIdentifier {

    private static final Logger LOGGER = Logger.getLogger(AbstractTrajectoryIntersectionIdentifier.class);

    /**
     * The method implementing the available algorithms for identifying trajectory collision.
     *
     * @param one             List of coordinates for the first UID
     * @param two             List of coordinates for the second UID
     * @param spatialEpsilon  Maximum spatial distance between two points to be considered a potential intersection
     * @param temporalEpsilon Maximum temporal interval between two points to be considered a potential intersection.
     * @return
     */
    public abstract Optional<Intersection> identify(List<EnhancedCoordinate> one,
                                                    List<EnhancedCoordinate> two,
                                                    Double spatialEpsilon,
                                                    Integer temporalEpsilon);

    /**
     * Perform primary validations before proceeding to actual computation. Please refer to
     * {@link #onDifferentFloors(List, List)}, {@link #noCoordinates(List, List)} and
     * {@link #sameUIDs(List, List)} methods for further details.
     *
     * @param one Coordinates for first UID
     * @param two Coordinates for second UID
     * @return
     */
    public boolean validate(List<EnhancedCoordinate> one,
                            List<EnhancedCoordinate> two) {
        return !noCoordinates(one, two) && !sameUIDs(one, two) && !onDifferentFloors(one, two);
    }

    /**
     * If given UIDs move on different floors throughout the day then there is no
     * point in proceeding with this computation
     *
     * @param one Coordinates for first UID
     * @param two Coordinates for second UID
     * @return true if UID include coordinates residing on different floors. false otherwise.
     */
    protected boolean onDifferentFloors(List<EnhancedCoordinate> one, List<EnhancedCoordinate> two) {
        Integer[] floorsFromFirst = one.stream().map(EnhancedCoordinate::getFloor).distinct().toArray(Integer[]::new);
        Integer[] floorsFromSecond = two.stream().map(EnhancedCoordinate::getFloor).distinct().toArray(Integer[]::new);
        if (floorsFromFirst.length == 1 && floorsFromFirst.length == floorsFromSecond.length && floorsFromFirst[0] != floorsFromSecond[0]) {
            LOGGER.info("All measurements reside on different floors");
            return true;
        }

        return false;
    }

    /**
     * Check whether one of the lists of coordinates is empty or null.
     *
     * @param one Coordinates for first UID
     * @param two Coordinates for second UID
     * @return True if either of the lists is empty or null.
     */
    protected boolean noCoordinates(List<EnhancedCoordinate> one,
                                    List<EnhancedCoordinate> two) {
        return CollectionUtils.isEmpty(one) || CollectionUtils.isEmpty(two);
    }

    /**
     * Check if the user requested an intersection identification between coordinates of the same UID.
     *
     * @param one Coordinates for first UID
     * @param two Coordinates for second UID
     * @return true if both one and two refer to the same UID. false otherwise.
     */
    protected boolean sameUIDs(List<EnhancedCoordinate> one,
                               List<EnhancedCoordinate> two) {
        if (one.get(0).getUid().equals(two.get(0).getUid())) {
            LOGGER.info("No point in identifying intersection points of same UID");
            return true;
        }
        return false;
    }

    /**
     * Identifier to be used by the {@link TrajectoryIntersectionIdentifierFactory} as the criteria for choosing the
     * algorithm of computation.
     *
     * @return
     */
    public abstract String getName();

}
