package eu.sqlrose.core;

import static eu.sqlrose.core.E.DS_CANNOT_CONNECT;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Sep 05, 2016
 */
public class CannotConnectToDataSource extends SqlRoseException {

    private DataSource dataSource;

    public CannotConnectToDataSource(DataSource dataSource) {
        super(DS_CANNOT_CONNECT);
        this.dataSource = dataSource;
    }

    public CannotConnectToDataSource(DataSource dataSource, Throwable cause) {
        super(DS_CANNOT_CONNECT, cause);
        this.dataSource = dataSource;
    }
}
