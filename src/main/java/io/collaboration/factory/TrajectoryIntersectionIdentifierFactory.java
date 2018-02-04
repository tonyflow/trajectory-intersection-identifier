package io.collaboration.factory;

import io.collaboration.service.AbstractTrajectoryIntersectionIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Factory pattern abstracting the underlying implementation of {@link AbstractTrajectoryIntersectionIdentifier}
 * extensions. By specifying the name of the algorithm the respective algorithm will be used as the computational
 * model for the iteration of the application.
 */
@Component
public class TrajectoryIntersectionIdentifierFactory {

    @Autowired
    private Map<String, AbstractTrajectoryIntersectionIdentifier> identifiers;


    public AbstractTrajectoryIntersectionIdentifier getIdentifier(String name) throws NoSuchAlgorithmException {
        return identifiers.entrySet().stream()
                .filter(e -> e.getKey().toLowerCase().contains(name))
                .map(algo -> algo.getValue())
                .findFirst().orElseThrow(NoSuchAlgorithmException::new);
    }

    public Set<String> availableIdentifiers() {
        return identifiers.values().stream().map(i -> i.getName()).collect(toSet());
    }
}
