package eu.sqlrose.core;

import eu.sqlrose.core.DataSourceConnectionException.CannotConnectToDataSource;
import eu.sqlrose.core.DataSourceConnectionException.CannotDisconnectFromDataSource;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

import static java.util.Arrays.copyOf;
import static org.apache.commons.lang3.Validate.notBlank;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 08, 2016
 */
public abstract class DataSource implements Serializable {

    private final String name;

    private final String description;

    private final String username;

    private final char[] password;

    protected DataSource(String name, String description, String username, char[] password) {
        this.name = notBlank(name, "the data source name cannot be null, empty or whitespace-only");
        this.description = description;

        this.username = username;
        this.password = password == null ? null : copyOf(password, password.length);
    }

    protected DataSource(String name, String description, String username, String password) {
        this(name, description, username, password == null ? null : password.toCharArray());
    }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public String getUsername() { return username; }

    public String getPassword() { return password == null ? null : new String(password); }

    public abstract boolean isConnected();

    public abstract void connect() throws CannotConnectToDataSource;

    public abstract void disconnect() throws CannotDisconnectFromDataSource;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(name);

        if (description != null) {
            builder.append(" (");
            if (description.length() < 51) {
                builder.append(description);
            } else {
                builder.append(description.substring(0, 50)).append("...");
            }
            builder.append(')');
        }

        if (username != null) {
            builder.append(' ').append(username).append('/');
            if (password != null && password.length > 0) {
                builder.append("*****");
            }
        }

        return builder.toString();
    }

    @Override
    public boolean equals(Object that) {
        return this == that || that != null && that instanceof DataSource &&
                               new EqualsBuilder().append(name, ((DataSource) that).name).isEquals();
    }

    @Override
    public int hashCode() { return new HashCodeBuilder().append(name).toHashCode(); }
}
