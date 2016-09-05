package eu.sqlrose.core;

import static eu.sqlrose.core.E.DS_CANNOT_DISCONNECT;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Sep 05, 2016
 */
public class CannotDisconnectFromDataSource extends SqlRoseException {

    private DataSource dataSource;

    public CannotDisconnectFromDataSource(DataSource dataSource) {
        super(DS_CANNOT_DISCONNECT);
        this.dataSource = dataSource;
    }

    public CannotDisconnectFromDataSource(DataSource dataSource, Throwable cause) {
        super(DS_CANNOT_DISCONNECT, cause);
        this.dataSource = dataSource;
    }
}
