package eu.sqlrose.core;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

import static java.util.Arrays.copyOf;
import static org.apache.commons.lang3.Validate.notBlank;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 08, 2016
 */
public abstract class ConnectionInfo implements Serializable {

    protected final String name;

    protected final String description;

    protected final String username;

    protected final byte[] password;

    protected ConnectionInfo(String name, String description, String username, byte[] password) {
        this.name = notBlank(name, "the connection name cannot be null, empty or whitespace-only");
        this.description = description;

        this.username = username;
        this.password = password == null ? null : copyOf(password, password.length);
    }

    protected ConnectionInfo(String name, String description, String username, String password) {
        this(name, description, username, password == null ? null : password.getBytes());
    }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public String getUsername() { return username; }

    public String getPassword() { return password == null ? null : new String(password); }

    @Override
    public boolean equals(Object that) {
        return this == that || !(that == null || !(that instanceof ConnectionInfo)) &&
                               new EqualsBuilder().append(name, ((ConnectionInfo) that).name).isEquals();
    }

    @Override
    public int hashCode() { return new HashCodeBuilder(17, 37).append(name).toHashCode(); }
}
