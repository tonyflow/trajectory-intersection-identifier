package io.collaboration.api;

import io.collaboration.helper.EuclideanDistance2D;

import java.util.Objects;

/**
 * DTO carrying the result of the intersecting trajectories
 */
public class Intersection {

    /**
     * Planar point for the first UID
     */
    private EnhancedCoordinate pointOne;

    /**
     * Planar point for the second UID
     */
    private EnhancedCoordinate pointTwo;

    /**
     * The distance between the points when the intersection occurred.
     */
    private Double euclideanDistance;

    public Intersection() {
    }

    public Intersection(EnhancedCoordinate pointOne,
                        EnhancedCoordinate pointTwo) {
        this.pointOne = Objects.requireNonNull(pointOne);
        this.pointTwo = Objects.requireNonNull(pointTwo);
        this.euclideanDistance = EuclideanDistance2D.getDistance(pointOne.getX(),
                pointOne.getY(),
                pointTwo.getX(),
                pointTwo.getY());
    }

    public EnhancedCoordinate getPointOne() {
        return pointOne;
    }

    public void setPointOne(EnhancedCoordinate pointOne) {
        this.pointOne = pointOne;
    }

    public EnhancedCoordinate getPointTwo() {
        return pointTwo;
    }

    public void setPointTwo(EnhancedCoordinate pointTwo) {
        this.pointTwo = pointTwo;
    }

    public Double getEuclideanDistance() {
        return euclideanDistance;
    }

    public void setEuclideanDistance(Double euclideanDistance) {
        this.euclideanDistance = euclideanDistance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Intersection that = (Intersection) o;

        if (pointOne != null ? !pointOne.equals(that.pointOne) : that.pointOne != null) return false;
        if (pointTwo != null ? !pointTwo.equals(that.pointTwo) : that.pointTwo != null) return false;
        return euclideanDistance != null ? euclideanDistance.equals(that.euclideanDistance) : that.euclideanDistance == null;
    }

    @Override
    public int hashCode() {
        int result = pointOne != null ? pointOne.hashCode() : 0;
        result = 31 * result + (pointTwo != null ? pointTwo.hashCode() : 0);
        result = 31 * result + (euclideanDistance != null ? euclideanDistance.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Intersection{" +
                "pointOne=" + pointOne +
                ", pointTwo=" + pointTwo +
                ", euclideanDistance=" + euclideanDistance +
                '}';
    }
}
