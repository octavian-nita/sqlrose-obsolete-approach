package eu.sqlrose.core;

import org.apache.commons.lang3.Validate;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 08, 2016
 */
public class JdbcDataSource extends DataSource {

    private final String url;

    private final String driverClass;

    private final Properties properties;

    private final boolean urlPartsAvailable;

    // The URL-part fields may be null or empty if the URL is directly provided.
    // It is rather difficult to reliably parse every type of JDBC-specific URL,
    // see for example http://www.h2database.com/html/features.html#database_url

    private final String dbms;

    private final String host;

    private final Integer port;

    private final String dbName;

    protected JdbcDataSource(Builder builder) {
        super(Validate.notNull(builder, "The data source details cannot be null or empty").name, builder.description,
              builder.username, builder.password);

        this.driverClass = Validate
            .notBlank(builder.driverClass, "The JDBC driver class name cannot be null, empty or whitespace-only");

        // The db URL could be specified either wholly or by components (sub-protocol, host, port, db name, etc.):
        if (isNotBlank(builder.url)) {

            this.url = builder.url;
            this.dbms = null;
            this.host = null;
            this.port = null;
            this.dbName = null;
            this.urlPartsAvailable = false; // don't try to parse, it is difficult and of little value

        } else if (isNotBlank(builder.dbms) && isNotBlank(builder.dbName)) {

            StringBuilder url = new StringBuilder("jdbc:").append(builder.dbms);
            if (isNotBlank(builder.host)) {
                url.append("//").append(builder.host);

                if (builder.port != null) {
                    if (builder.port < 0) {
                        throw new IllegalArgumentException(
                            "If specified, the database port has to be a positive integer");
                    }
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
                "Either the database URL or its protocol and name have to be specified (i.e. non-blank)");
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

    public Properties getProperties() { return properties; }

    public boolean hasUrlPartsAvailable() { return urlPartsAvailable; }

    public String getDbms() { return dbms; }

    public String getHost() { return host; }

    public Integer getPort() { return port; }

    public String getDbName() { return dbName; }

    private transient Connection connection;

    @Override
    public boolean isConnected() { return connection != null; }

    @Override
    public void connect() throws CannotConnectToDataSource {
        if (isConnected()) {
            LoggerFactory.getLogger(getClass()).warn("A connection to the " + getName() +
                                                     " data source has already been established;" +
                                                     " ignoring request to connect...");
            return;
        }

        try {
            final Properties info = new Properties();

            Properties properties = getProperties();
            if (properties != null && !properties.isEmpty()) {
                info.putAll(properties);
            }
            if (isNotBlank(getUsername())) {
                info.put("user", getUsername());
            }
            if (isNotBlank(getPassword())) {
                info.put("password", getPassword());
            }

            Class.forName(getDriverClass());
            connection = DriverManager.getConnection(getUrl(), info);

        } catch (ClassNotFoundException | SQLException ex) {
            throw new CannotConnectToDataSource(this, ex);
        }
    }

    @Override
    public void disconnect() throws CannotDisconnectFromDataSource {
        if (isConnected()) {
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

        private Integer port;

        public Builder port(Integer port) {
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
