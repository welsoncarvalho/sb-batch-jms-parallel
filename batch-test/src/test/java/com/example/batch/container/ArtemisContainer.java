package com.example.batch.container;

import org.testcontainers.containers.GenericContainer;

public class ArtemisContainer extends GenericContainer<ArtemisContainer> {
    private static final String ARTEMIS_IMAGE = "welsonfscarvalho/artemis-tst-rem-chunk:0.1";
    private static final Integer ARTEMIS_PORT = 61616;

    public ArtemisContainer() {
        super(ARTEMIS_IMAGE);
        addExposedPort(ARTEMIS_PORT);
    }

    public Integer getPort() {
        return getMappedPort(ARTEMIS_PORT);
    }
}
