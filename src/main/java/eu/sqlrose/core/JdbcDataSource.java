package eu.sqlrose.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 08, 2016
 */
public class JdbcDataSource extends DataSource {

    private final String url;

    private final String driverClass;

    private final Properties properties;

    private final boolean urlPartsAvailable;

    // The URL-part fields may be null / empty / 0 if the URL is provided directly.
    // It is rather difficult to reliably parse any type of JDBC-specific URL - see
    // for example: http://www.h2database.com/html/features.html#database_url

    private final String dbms;

    private final String host;

    private final int port;

    private final String dbName;

    protected JdbcDataSource(Builder builder) {
        super(Validate.notNull(builder, "The data source details cannot be null or empty").name, builder.description,
              builder.username, builder.password);

        this.driverClass = Validate
            .notBlank(builder.driverClass, "The JDBC driver class name cannot be null, empty or whitespace-only");

        // The db URL could be specified either wholly or by components (sub-protocol, host, port, db name, etc.):
        if (!StringUtils.isBlank(builder.url)) {

            this.url = builder.url;
            this.dbms = "";
            this.host = "";
            this.port = 0;
            this.dbName = "";
            this.urlPartsAvailable = false;

        } else if (!(StringUtils.isBlank(builder.dbms) || StringUtils.isBlank(builder.dbName)) && builder.port >= 0) {

            StringBuilder url = new StringBuilder("jdbc:").append(builder.dbms);
            if (!StringUtils.isBlank(builder.host)) {
                url.append("//").append(builder.host);
                if (builder.port > 0) {
                    url.append(":").append(builder.port);
                }
                url.append("/");
            }

            this.url = url.append(":").append(builder.dbName).toString();
            this.dbms = builder.dbms;
            this.host = builder.host;
            this.port = builder.port;
            this.dbName = builder.dbName;
            this.urlPartsAvailable = true;

        } else {
            throw new IllegalArgumentException(
                "Either the database URL or its protocol and name have to be specified (i.e. non-blank) and if " +
                "specified, the port has to be a positive integer");
        }

        if (builder.properties != null && !builder.properties.isEmpty()) {
            this.properties = new Properties();
            this.properties.putAll(builder.properties);
        } else {
            this.properties = null;
        }
    }

    public String getUrl() { return url; }

    public String getDriverClass() { return driverClass; }

    public boolean hasUrlPartsAvailable() { return urlPartsAvailable; }

    public String getDbms() { return dbms; }

    public String getHost() { return host; }

    public int getPort() { return port; }

    public String getDbName() { return dbName; }

    public Properties getProperties() { return properties; }

    private transient Connection connection;

    @Override
    public boolean isConnected() { return connection == null; }

    @Override
    public void connect() throws CannotConnectToDataSource {
        if (connection != null) {
            LoggerFactory.getLogger(getClass()).warn("A connection to the " + getName() +
                                                     " data source has already been established;" +
                                                     " ignoring request to connect...");
            return;
        }

        try {
            Class.forName(getDriverClass());

            if (StringUtils.isNotBlank(getUsername())) {
                connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
            } else if (getProperties() != null) {
                connection = DriverManager.getConnection(getUrl(), getProperties());
            } else {
                connection = DriverManager.getConnection(getUrl());
            }
        } catch (ClassNotFoundException | SQLException ex) {
            throw new CannotConnectToDataSource(this, ex);
        }
    }

    @Override
    public void disconnect() throws CannotDisconnectFromDataSource {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException ex) {
                throw new CannotDisconnectFromDataSource(this, ex);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder =
            new StringBuilder(super.toString()).append(" [").append(driverClass).append("] ").append(url);

        if (properties != null && !properties.isEmpty()) {
            for (Map.Entry<?, ?> entry : properties.entrySet()) {
                builder.append(' ').append(entry.getKey()).append('=').append(entry.getValue());
            }
        }

        return builder.toString();
    }

    public static class Builder {

        public JdbcDataSource build() { return new JdbcDataSource(this); }

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

        private char[] password;

        public Builder password(String password) {
            this.password = password == null ? null : password.toCharArray();
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
            if (properties != null && !properties.isEmpty()) {
                if (this.properties == null) {
                    this.properties = new Properties();
                }
                this.properties.putAll(properties);
            }
            return this;
        }

        public Builder property(String name, Object value) {
            if (name != null) {
                if (this.properties == null) {
                    this.properties = new Properties();
                }
                this.properties.put(name, value);
            }
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
