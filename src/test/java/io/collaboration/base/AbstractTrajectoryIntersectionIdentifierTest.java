package io.collaboration.base;

import io.collaboration.service.GabrielDelaunayTrajectoryIntersectionIdentifierTest;
import io.collaboration.TrajectoryIntersectionIdentifierApplication;
import io.collaboration.helper.DataLoader;
import io.collaboration.service.BruteTrajectoryIntersectionIdentifierTest;
import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static java.util.stream.Collectors.groupingBy;

/**
 * Constructs context for all test suites. Responsible for injecting the required dependencies in
 * {@link GabrielDelaunayTrajectoryIntersectionIdentifierTest} and {@link BruteTrajectoryIntersectionIdentifierTest}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TrajectoryIntersectionIdentifierApplication.class})
public class AbstractTrajectoryIntersectionIdentifierTest {

    private static final Logger LOGGER = Logger.getLogger(AbstractTrajectoryIntersectionIdentifierTest.class);

    @Autowired
    protected DataLoader dataLoader;
}
