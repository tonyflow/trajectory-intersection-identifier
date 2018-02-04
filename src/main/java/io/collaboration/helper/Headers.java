package io.collaboration.helper;

/**
 * Utility enum used by the Apache Commons parser, representing the headers of the reduced.csv.
 */
public enum Headers {

    TIMESTAMP("timestamp"),
    X_COORDINATE("x"),
    Y_COORDINATE("y"),
    FLOOR("floor"),
    UID("uid");

    private String name;

    Headers(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
