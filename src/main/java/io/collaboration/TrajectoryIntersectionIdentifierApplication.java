package io.collaboration;

import io.collaboration.api.ConfigurationProperties;
import io.collaboration.api.EnhancedCoordinate;
import io.collaboration.api.Intersection;
import io.collaboration.factory.TrajectoryIntersectionIdentifierFactory;
import io.collaboration.helper.ConfigurationReader;
import io.collaboration.helper.DataLoader;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Entry point of the TrajectoryIntersectionIdentifier application. The class is used to load the
 * application context, retrieve he configuration properties from the user while the only command
 * line argument is the name of the file that contains the CSV data). Afterwards, it invokes the
 * intersection identification method through a trajectory identifiers factory.
 */
@SpringBootApplication
public class TrajectoryIntersectionIdentifierApplication implements CommandLineRunner {

    private static final Logger LOGGER = Logger.getLogger(TrajectoryIntersectionIdentifierApplication.class);

    @Autowired
    private DataLoader dataLoader;

    @Autowired
    private ConfigurationReader configurationReader;

    @Autowired
    private TrajectoryIntersectionIdentifierFactory identifierFactoryFactory;

    public static void main(String[] args) {
        SpringApplication.run(TrajectoryIntersectionIdentifierApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Map<String, List<EnhancedCoordinate>> data = dataLoader.load(args[0]);

        LOGGER.info("Please provide name of algorithm for computation. Available algorithms: " + identifierFactoryFactory.availableIdentifiers());

        Scanner scanner = new Scanner(System.in);

        String next = "yes";

        while (next.equals("yes")) {

            ConfigurationProperties properties = configurationReader.read();

            long start = System.currentTimeMillis();

            LOGGER.debug("Starting computation for trajectory intersection between " + properties.getUIDOne()
                    + " and " + properties.getUIDTwo());

            Optional<Intersection> intersection = identifierFactoryFactory.getIdentifier(properties.getAlgorithm())
                    .identify(data.get(properties.getUIDOne()),
                            data.get(properties.getUIDTwo()),
                            properties.getSpatialEpsilon(),
                            properties.getTemporalEpsilon());

            long end = System.currentTimeMillis();


            if (intersection.isPresent()) {
                System.out.println("UID " + intersection.get().getPointOne().getUid() + " intersected with " + intersection.get().getPointTwo().getUid() + " on floor " + intersection.get().getPointOne().getFloor());
                System.out.println(intersection.get().getPointOne().getUid() + " coordinates during meeting: [" + intersection.get().getPointOne().getX() + "," + intersection.get().getPointOne().getY() + "], at " + intersection.get().getPointOne().getTimestamp());
                System.out.println(intersection.get().getPointTwo().getUid() + " coordinates during meeting: [" + intersection.get().getPointTwo().getX() + "," + intersection.get().getPointTwo().getY() + "], at " + intersection.get().getPointTwo().getTimestamp());
            } else {
                System.out.println("No intersection found between the given points");
            }

            System.out.println("Time of computation: " + (end - start) + " millis");

            System.out.print("Do you wish to continue? (yes / no): ");
            next = scanner.nextLine();
        }


    }

}
