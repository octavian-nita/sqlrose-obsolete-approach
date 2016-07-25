package eu.sqlrose.core;

import java.util.Map;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 08, 2016
 */
public class DriverBasedDataSource extends DataSource {

    private final String driverClass;

    private final String url;

    private final Properties properties;

    protected DriverBasedDataSource(Builder builder) {
        super(notNull(builder, "the data source name cannot be null, empty or whitespace-only").name,
              builder.description, builder.username, builder.password);

        this.driverClass =
            notBlank(builder.driverClass, "the driver class name cannot be null, empty or whitespace-only");

        // The db URL could be specified either wholly or by components (sub-protocol, host, port, db name, etc.):
        if (!isBlank(builder.url)) {

            this.url = builder.url;

        } else if (!(isBlank(builder.dbms) || isBlank(builder.dbName)) && builder.port >= 0) {

            StringBuilder url = new StringBuilder("jdbc:").append(builder.dbms);
            if (!isBlank(builder.host)) {
                url.append("//").append(builder.host);
                if (builder.port > 0) {
                    url.append(":").append(builder.port);
                }
                url.append("/");
            }
            this.url = url.append(":").append(builder.dbName).toString();

        } else {
            throw new IllegalArgumentException(
                "either the database URL or its protocol and name have to be specified (i.e. non-blank) and if " +
                "specified, the port has to be a positive integer");
        }

        if (builder.properties != null && !builder.properties.isEmpty()) {
            this.properties = new Properties();
            this.properties.putAll(builder.properties);
        } else {
            this.properties = null;
        }
    }

    public String getDriverClass() { return driverClass; }

    public String getUrl() { return url; }

    public Properties getProperties() { return properties; }

    @Override
    public String toString() {
        StringBuilder builder =
            new StringBuilder(super.toString()).append(' ').append(driverClass).append(' ').append(url);

        if (properties != null && !properties.isEmpty()) {
            for (Map.Entry<?, ?> entry : properties.entrySet()) {
                builder.append(' ').append(entry.getKey()).append('=').append(entry.getValue());
            }
        }

        return builder.toString();
    }

    public static class Builder {

        public DriverBasedDataSource build() { return new DriverBasedDataSource(this); }

        private String name;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        private String description;

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        private String username;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        private byte[] password;

        public Builder password(String password) {
            this.password = password == null ? null : password.getBytes();
            return this;
        }

        private String driverClass;

        public Builder driverClass(String driverClass) {
            this.driverClass = driverClass;
            return this;
        }

        private String url;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        private Properties properties;

        public Builder properties(Properties properties) {
            if (this.properties == null) {
                this.properties = new Properties();
            }
            this.properties.putAll(properties);
            return this;
        }

        public Builder property(String name, Object value) {
            if (this.properties == null) {
                this.properties = new Properties();
            }
            this.properties.put(name, value);
            return this;
        }

        /**
         * The dbms (protocol) could include an optional subsubprotocol, e.g.
         * <a href="https://db.apache.org/derby/docs/10.8/devguide/cdevdvlp17453.html">
         * jdbc:derby:[subsubprotocol:][databaseName][;attribute=value]*</a>.
         */
        private String dbms;

        public Builder dbms(String dbms) {
            this.dbms = dbms;
            return this;
        }

        private String host;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        private int port;

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        private String dbName;

        public Builder dbName(String dbName) {
            this.dbName = dbName;
            return this;
        }
    }
}
