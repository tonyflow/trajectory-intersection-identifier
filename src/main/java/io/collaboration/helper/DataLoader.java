package io.collaboration.helper;

import io.collaboration.api.EnhancedCoordinate;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.groupingByConcurrent;

/**
 * Reads CSV files containing the coordinates of users in space and performs a simple ETL by loading all
 * (timestamp,x_coordinate,y_coordinate,floor,uid) combination to {@link EnhancedCoordinate} DTOs. Afterwards,
 * a map correlating the included UIDs with their respective coordinates through the day is created.
 */
@Component
public class DataLoader {

    private static final Logger LOGGER = Logger.getLogger(DataLoader.class);

    public Map<String, List<EnhancedCoordinate>> load(String fileName) throws IOException {

        LOGGER.info("Preparing data from file " + fileName + " ...");

        Reader reader = new FileReader(fileName);
        CSVParser parser = CSVFormat.DEFAULT.withHeader(Headers.class)
                .withFirstRecordAsHeader()
                .parse(reader);

        Map<String, List<EnhancedCoordinate>> collect = StreamSupport.stream(parser.spliterator(), true)
                .map(r -> new EnhancedCoordinate(r.get(Headers.UID),
                        ZonedDateTime.parse(r.get(Headers.TIMESTAMP)),
                        Double.parseDouble(r.get(Headers.X_COORDINATE)),
                        Double.parseDouble(r.get(Headers.Y_COORDINATE)),
                        Integer.valueOf(r.get(Headers.FLOOR))))
                .collect(groupingByConcurrent(EnhancedCoordinate::getUid));

        LOGGER.info("Data from file " + fileName + " ready");

        return collect;
    }
}
