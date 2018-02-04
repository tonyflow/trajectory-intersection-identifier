package io.collaboration.api;

import org.kynosarges.tektosyne.geometry.PointD;

import java.time.ZonedDateTime;

/**
 * During data loading, the {@link EnhancedCoordinate} is an enriched type of
 * coordinate used to carry the information included in the CSV file.
 */
public class EnhancedCoordinate {

    /**
     *
     */
    private String uid;

    /**
     * {@link ZonedDateTime} the coordinate was recorded
     */
    private ZonedDateTime timestamp;

    /**
     * Distance from x axis
     */
    private Double x;

    /**
     * Distance from y axis
     */
    private Double y;

    /**
     * The number of the floor the user with {@link #uid} was moving
     * when the coordinate was recorded.
     */
    private Integer floor;

    public EnhancedCoordinate() {
    }

    public EnhancedCoordinate(String uid,
                              ZonedDateTime timestamp,
                              Double x,
                              Double y,
                              Integer floor) {
        this.uid = uid;
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.floor = floor;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnhancedCoordinate that = (EnhancedCoordinate) o;

        if (Double.compare(that.x, x) != 0) return false;
        if (Double.compare(that.y, y) != 0) return false;
        if (floor != that.floor) return false;
        if (uid != null ? !uid.equals(that.uid) : that.uid != null) return false;
        return timestamp != null ? timestamp.equals(that.timestamp) : that.timestamp == null;
    }

    public boolean equals(PointD point) {
        return point.x == this.x && point.y == this.y;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = uid != null ? uid.hashCode() : 0;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        temp = Double.doubleToLongBits(x);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + floor;
        return result;
    }

    @Override
    public String toString() {
        return "EnhancedCoordinate{" +
                "uid='" + uid + '\'' +
                ", timestamp=" + timestamp +
                ", x=" + x +
                ", y=" + y +
                ", floor=" + floor +
                '}';
    }

}
