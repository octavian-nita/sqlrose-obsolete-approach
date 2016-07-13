package eu.sqlrose.core;

import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.Validate.notBlank;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 08, 2016
 */
public class JdbcConnectionInfo extends ConnectionInfo {

    protected final String driverClass;

    protected final String url;

    protected final Properties properties;

    protected JdbcConnectionInfo(Builder builder) {
        super(builder.name, builder.description, builder.username, builder.password);

        this.driverClass =
            notBlank(builder.driverClass, "the driver class name cannot be null, empty or whitespace-only");

        // The db URL could be specified either wholly or by components (sub-protocol, host, port, db name, etc.):
        if (!isBlank(builder.url)) {

            this.url = builder.url;

        } else if (!(isBlank(builder.dbms) || isBlank(builder.dbName))) {

            StringBuilder url = new StringBuilder("jdbc:").append(builder.dbms);
            if (!isBlank(builder.host)) {
                url.append("//").append(builder.host);
                if (!isBlank(builder.port)) {
                    url.append(":").append(builder.port);
                }
                url.append("/");
            }
            this.url = url.append(":").append(builder.dbName).toString();

        } else {
            throw new IllegalArgumentException(
                "either the database URL or its protocol and name have to be specified (i.e. non-blank)");
        }

        if (builder.properties != null && !builder.properties.isEmpty()) {
            this.properties = new Properties();
            this.properties.putAll(properties);
        } else {
            this.properties = null;
        }
    }

    public String getDriverClass() { return driverClass; }

    public String getUrl() { return url; }

    public Properties getProperties() { return properties; }

    public static class Builder {

        public JdbcConnectionInfo build() { return new JdbcConnectionInfo(this); }

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

        private String port;

        public Builder port(String port) {
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
