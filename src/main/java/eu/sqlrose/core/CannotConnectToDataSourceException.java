package eu.sqlrose.core;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 28, 2016
 */
public class CannotConnectToDataSourceException extends Exception {

    private final DataSource dataSource;

    protected CannotConnectToDataSourceException(Builder builder) {
        super(builder.message, builder.cause);
        this.dataSource = builder.dataSource;
    }

    public DataSource getDataSource() { return dataSource; }

    public static class Builder {

        private String message;

        private Throwable cause;

        private DataSource dataSource;

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public Builder dataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public CannotConnectToDataSourceException build() {
            return new CannotConnectToDataSourceException(this);
        }
    }
}
