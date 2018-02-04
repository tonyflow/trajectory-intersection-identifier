package io.collaboration.helper;

import io.collaboration.api.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * Utility class for populating {@link ConfigurationProperties} for each run of the application.
 */
@Component
public class ConfigurationReader {

    private static final int MAX_TEMPORAL_VALUE = 60;
    private static final double MAX_SPATIAL_EPISON = 10;

    public ConfigurationProperties read() {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Algorithm: ");
        String algorithm = scanner.nextLine();

        double spatialEpsilon = Double.MAX_VALUE;
        while (spatialEpsilon > MAX_SPATIAL_EPISON) {
            System.out.print("Provide spatial epsilon (<10): ");
            spatialEpsilon = scanner.nextDouble();
        }


        int temporalEpsilon = Integer.MAX_VALUE;
        while (temporalEpsilon > MAX_TEMPORAL_VALUE) {
            System.out.print("Provide temporal epsilon in seconds (max = 60 seconds): ");
            temporalEpsilon = scanner.nextInt();
        }
        scanner.nextLine();

        System.out.print("Provide first UID: ");
        String firstTrajectory = scanner.nextLine();

        System.out.print("Provide second UID: ");
        String secondTrajectory = scanner.nextLine();

        return new ConfigurationProperties(algorithm,
                spatialEpsilon,
                temporalEpsilon,
                firstTrajectory,
                secondTrajectory);

    }
}
