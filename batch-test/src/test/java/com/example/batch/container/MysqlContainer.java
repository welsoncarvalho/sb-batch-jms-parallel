package com.example.batch.container;

import org.testcontainers.containers.GenericContainer;

public class MysqlContainer extends GenericContainer<MysqlContainer> {
    private static final String MYSQL_IMAGE = "welsonfscarvalho/mysql-tst-rem-chunk:0.1";
    private static final Integer MYSQL_PORT = 3306;

    public MysqlContainer() {
        super(MYSQL_IMAGE);
        addExposedPort(MYSQL_PORT);
    }

    public Integer getPort() {
        return getMappedPort(MYSQL_PORT);
    }
}
