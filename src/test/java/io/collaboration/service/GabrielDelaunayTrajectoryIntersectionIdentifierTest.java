package io.collaboration.service;

import io.collaboration.api.Intersection;
import io.collaboration.service.impl.GabrielDelaunayTrajectoryIntersectionIdentifier;
import io.collaboration.api.EnhancedCoordinate;
import io.collaboration.base.AbstractTrajectoryIntersectionIdentifierTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Asserting appropriate behavior of {@link GabrielDelaunayTrajectoryIntersectionIdentifier} implementation.
 */
public class GabrielDelaunayTrajectoryIntersectionIdentifierTest extends AbstractTrajectoryIntersectionIdentifierTest {

    @Autowired
    private GabrielDelaunayTrajectoryIntersectionIdentifier identifier;

    @Test
    public void testIntersecting() throws Exception {
        URL csv = this.getClass().getResource("/intersecting.csv");

        Map<String, List<EnhancedCoordinate>> data = dataLoader.load(csv.getPath());

        Optional<Intersection> intersection = identifier.identify(data.get("de0a13a8"),
                data.get("2f442867"),
                3.6,
                30);

        Assert.assertTrue(intersection.isPresent());

        Intersection result = intersection.get();
        Assert.assertTrue(result.getEuclideanDistance() <= 3.6);
        Assert.assertTrue(ChronoUnit.SECONDS.between(result.getPointOne().getTimestamp(), result.getPointTwo().getTimestamp()) <= 30);
    }

    @Test
    public void testNonIntersecting() throws Exception {

        URL csv = this.getClass().getResource("/non-intersecting.csv");

        Map<String, List<EnhancedCoordinate>> data = dataLoader.load(csv.getPath());

        Optional<Intersection> intersection = identifier.identify(data.get("287279d1"),
                data.get("8f719572"),
                3.4,
                30);

        Assert.assertFalse(intersection.isPresent());

    }

}