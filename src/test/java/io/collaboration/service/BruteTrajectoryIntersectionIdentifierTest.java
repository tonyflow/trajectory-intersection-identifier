package io.collaboration.service;

import io.collaboration.api.EnhancedCoordinate;
import io.collaboration.api.Intersection;
import io.collaboration.base.AbstractTrajectoryIntersectionIdentifierTest;
import io.collaboration.service.impl.BruteTrajectoryIntersectionIdentifier;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BruteTrajectoryIntersectionIdentifierTest extends AbstractTrajectoryIntersectionIdentifierTest {

    @Autowired
    private BruteTrajectoryIntersectionIdentifier identifier;

    @Test
    @Ignore
    public void testFindAllIntersectionsOrNoneIntersections() throws Exception {

        Map<String, List<EnhancedCoordinate>> data = dataLoader.load("/Users/niko/code/THT/reduced.csv");

        for (Map.Entry<String, List<EnhancedCoordinate>> entry : data.entrySet()) {
            if (entry.getKey().equals("de0a13a8")) continue;

            System.out.println("Checking against " + entry.getKey());
            Optional<Intersection> intersection = identifier.identify(data.get("de0a13a8"),
                    entry.getValue(),
                    3.4,
                    30);

            if (intersection.isPresent()) {
                System.out.println("UID de0a13a8 intersected with " + entry.getKey() + " on floor " + intersection.get().getPointOne().getFloor());
                System.out.println("de0a13a8 coordinates during meeting: [" + intersection.get().getPointOne().getX() + "," + intersection.get().getPointOne().getY() + "], at " + intersection.get().getPointOne().getTimestamp());
                System.out.println(entry.getKey() + " coordinates during meeting: [" + intersection.get().getPointTwo().getX() + "," + intersection.get().getPointTwo().getY() + "], at " + intersection.get().getPointTwo().getTimestamp());
                System.out.println(intersection.get().toString());
            }
        }
    }

    @Test
    public void testIntersecting() throws Exception {
        URL csv = this.getClass().getResource("/intersecting.csv");

        Map<String, List<EnhancedCoordinate>> data = dataLoader.load(csv.getPath());

        Optional<Intersection> intersection = identifier.identify(data.get("de0a13a8"),
                data.get("2f442867"),
                3.7,
                30);

        Assert.assertTrue(intersection.isPresent());

        Intersection result = intersection.get();
        Assert.assertTrue(result.getEuclideanDistance() <= 3.7);
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

    @Test
    public void testDifferentFloors() throws Exception {

        URL csv = this.getClass().getResource("/different-floors.csv");

        Map<String, List<EnhancedCoordinate>> data = dataLoader.load(csv.getPath());

        Optional<Intersection> intersection = identifier.identify(data.get("de0a13a8"),
                data.get("2f442867"),
                3.4,
                30);

        Assert.assertFalse(intersection.isPresent());

    }

    @Test
    public void testEmptyCoordinates() throws Exception {
        URL csv = this.getClass().getResource("/different-floors.csv");

        Map<String, List<EnhancedCoordinate>> data = dataLoader.load(csv.getPath());

        Optional<Intersection> intersection = identifier.identify(null,
                data.get("2f442867"),
                3.4,
                30);

        Assert.assertFalse(intersection.isPresent());
    }

    @Test
    public void testSameUID() throws Exception {
        URL csv = this.getClass().getResource("/different-floors.csv");

        Map<String, List<EnhancedCoordinate>> data = dataLoader.load(csv.getPath());

        Optional<Intersection> intersection = identifier.identify(data.get("2f442867"),
                data.get("2f442867"),
                3.4,
                30);

        Assert.assertFalse(intersection.isPresent());
    }
}