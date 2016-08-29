package eu.sqlrose.core;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 28, 2016
 */
public class DataSourceConnectionException extends Exception {

    private final DataSource dataSource;

    public DataSource getDataSource() { return dataSource; }

    public DataSourceConnectionException(DataSource dataSource) { this(dataSource, null, null); }

    public DataSourceConnectionException(DataSource dataSource, String msg) { this(dataSource, msg, null); }

    public DataSourceConnectionException(DataSource dataSource, Throwable cause) { this(dataSource, null, cause); }

    public DataSourceConnectionException(DataSource dataSource, String msg, Throwable cause) {
        super(msg, cause);
        this.dataSource = dataSource;
    }

    public static class CannotConnectToDataSource extends DataSourceConnectionException {

        public CannotConnectToDataSource(DataSource dataSource) { this(dataSource, null, null); }

        public CannotConnectToDataSource(DataSource dataSource, String msg) { this(dataSource, msg, null); }

        public CannotConnectToDataSource(DataSource dataSource, Throwable cause) { this(dataSource, null, cause); }

        public CannotConnectToDataSource(DataSource dataSource, String msg, Throwable cause) {
            super(dataSource, msg != null
                              ? msg
                              : dataSource != null ? "Cannot connect to data source " + dataSource.getName() : null,
                  cause);
        }
    }

    public static class CannotDisconnectFromDataSource extends DataSourceConnectionException {

        public CannotDisconnectFromDataSource(DataSource dataSource) { this(dataSource, null, null); }

        public CannotDisconnectFromDataSource(DataSource dataSource, String msg) { this(dataSource, msg, null); }

        public CannotDisconnectFromDataSource(DataSource dataSource, Throwable cause) { this(dataSource, null, cause); }

        public CannotDisconnectFromDataSource(DataSource dataSource, String msg, Throwable cause) {
            super(dataSource, msg != null
                              ? msg
                              : dataSource != null
                                ? "Cannot disconnect from data source " + dataSource.getName()
                                : null, cause);
        }
    }
}
