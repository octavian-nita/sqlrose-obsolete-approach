package eu.sqlrose.core;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 08, 2016
 */
public abstract class DataSource implements Serializable {

    private final String name;

    private final String description;

    private final transient String username;

    private final transient char[] password;

    protected DataSource(String name, String description, String username, char[] password) {
        this.name = Validate.notBlank(name, "The data source name cannot be null, empty or whitespace-only");
        this.description = description;

        this.username = username;
        this.password = password == null ? null : Arrays.copyOf(password, password.length);
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

    /**
     * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
     * @version 1.0, Sep 05, 2016
     */
    public enum ErrorCode implements eu.sqlrose.core.ErrorCode {

        CANNOT_CONNECT,
        CANNOT_DISCONNECT;

        @Override
        public String getCode() { return "E_DS_" + name(); }
    }

    /**
     * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
     * @version 1.0, Sep 05, 2016
     */
    public static class CannotConnectToDataSource extends SqlRoseException {

        private DataSource dataSource;

        public CannotConnectToDataSource(DataSource dataSource) {
            super(ErrorCode.CANNOT_CONNECT);
            this.dataSource = dataSource;
        }

        public CannotConnectToDataSource(DataSource dataSource, Throwable cause) {
            super(ErrorCode.CANNOT_CONNECT, cause);
            this.dataSource = dataSource;
        }

        @Override
        public Object[] getDetails() {
            return dataSource == null ? super.getDetails() : new Object[]{dataSource.getName()};
        }
    }

    /**
     * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
     * @version 1.0, Sep 05, 2016
     */
    public static class CannotDisconnectFromDataSource extends SqlRoseException {

        private DataSource dataSource;

        public CannotDisconnectFromDataSource(DataSource dataSource) {
            super(ErrorCode.CANNOT_DISCONNECT);
            this.dataSource = dataSource;
        }

        public CannotDisconnectFromDataSource(DataSource dataSource, Throwable cause) {
            super(ErrorCode.CANNOT_DISCONNECT, cause);
            this.dataSource = dataSource;
        }

        @Override
        public Object[] getDetails() {
            return dataSource == null ? super.getDetails() : new Object[]{dataSource.getName()};
        }
    }
}
