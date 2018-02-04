package io.collaboration.api;

/**
 * DTO carrying the state of the configuration properties for each run of the
 * application.
 */
public class ConfigurationProperties {

    /**
     * Choose between gabriel or brute as a computation model
     * for the intersection identification.
     */
    private String algorithm;

    /**
     * Defines the maximum Euclidean distance between two planar points in
     * order for them to be considered as potential collision candidates.
     */
    private Double spatialEpsilon;

    /**
     * The maximum absolute difference between their timestamps, the coordinates must
     * satisfy in order to be intersection point candidates.
     */
    private Integer temporalEpsilon;

    /**
     * First UID under examination.
     */
    private String UIDOne;

    /**
     * Second UID under examination.*
     */
    private String UIDTwo;

    public ConfigurationProperties() {
    }

    public ConfigurationProperties(String algorithm,
                                   Double spatialEpsilon,
                                   Integer temporalEpsilon,
                                   String UIDOne,
                                   String UIDTwo) {
        this.algorithm = algorithm;
        this.spatialEpsilon = spatialEpsilon;
        this.temporalEpsilon = temporalEpsilon;
        this.UIDOne = UIDOne;
        this.UIDTwo = UIDTwo;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public Double getSpatialEpsilon() {
        return spatialEpsilon;
    }

    public void setSpatialEpsilon(Double spatialEpsilon) {
        this.spatialEpsilon = spatialEpsilon;
    }

    public Integer getTemporalEpsilon() {
        return temporalEpsilon;
    }

    public void setTemporalEpsilon(Integer temporalEpsilon) {
        this.temporalEpsilon = temporalEpsilon;
    }

    public String getUIDOne() {
        return UIDOne;
    }

    public void setUIDOne(String UIDOne) {
        this.UIDOne = UIDOne;
    }

    public String getUIDTwo() {
        return UIDTwo;
    }

    public void setUIDTwo(String UIDTwo) {
        this.UIDTwo = UIDTwo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConfigurationProperties that = (ConfigurationProperties) o;

        if (algorithm != null ? !algorithm.equals(that.algorithm) : that.algorithm != null) return false;
        if (spatialEpsilon != null ? !spatialEpsilon.equals(that.spatialEpsilon) : that.spatialEpsilon != null)
            return false;
        if (temporalEpsilon != null ? !temporalEpsilon.equals(that.temporalEpsilon) : that.temporalEpsilon != null)
            return false;
        if (UIDOne != null ? !UIDOne.equals(that.UIDOne) : that.UIDOne != null) return false;
        return UIDTwo != null ? UIDTwo.equals(that.UIDTwo) : that.UIDTwo == null;
    }

    @Override
    public int hashCode() {
        int result = algorithm != null ? algorithm.hashCode() : 0;
        result = 31 * result + (spatialEpsilon != null ? spatialEpsilon.hashCode() : 0);
        result = 31 * result + (temporalEpsilon != null ? temporalEpsilon.hashCode() : 0);
        result = 31 * result + (UIDOne != null ? UIDOne.hashCode() : 0);
        result = 31 * result + (UIDTwo != null ? UIDTwo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ConfigurationProperties{" +
                "algorithm='" + algorithm + '\'' +
                ", spatialEpsilon=" + spatialEpsilon +
                ", temporalEpsilon=" + temporalEpsilon +
                ", UIDOne='" + UIDOne + '\'' +
                ", UIDTwo='" + UIDTwo + '\'' +
                '}';
    }
}
