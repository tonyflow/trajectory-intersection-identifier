package io.collaboration.helper;

import io.collaboration.service.AbstractTrajectoryIntersectionIdentifier;

import static java.lang.Math.*;

/**
 * Helper class for evaluating the Euclidean distance between two planar points. This is
 * the ordinary straight line between two points in the Euclidean space. In other words,
 * the Euclidean distance between points p and q is the length of the line segment connecting
 * them (pq).
 */
public final class EuclideanDistance2D {

    /**
     * Method evaluating the Euclidean distance. Used by the {@link AbstractTrajectoryIntersectionIdentifier}
     * implementations for identifying points which are close to one another on the grounds of the
     * <strong>spatialEpsilon</strong> configuration property which is provided as user input.
     *
     * @param x1 Start x of the line segment
     * @param y1 Start y of the line segment
     * @param x2 End x of the line segment
     * @param y2 End y of the line segment
     * @return
     */
    public static Double getDistance(Double x1,
                                     Double y1,
                                     Double x2,
                                     Double y2) {

        return sqrt(pow(abs(x1 - x2), 2) + pow(abs(y1 - y2), 2));
    }
}
