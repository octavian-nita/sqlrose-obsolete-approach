package eu.sqlrose.core.error;

import java.io.Serializable;
import java.util.Objects;

/**
 * Error code whose {@link #getValue() value} can be resolved to a localized message when displaying the error, etc.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.1, Nov 27, 2017
 */
public class ErrorCode implements Serializable {

    public static final ErrorCode E_GENERIC = new ErrorCode("E_GENERIC");

    public static final ErrorCode E_COMPOSITE = new ErrorCode("E_COMPOSITE");

    private final String value;

    public ErrorCode() { this("E_GENERIC"); }

    public ErrorCode(String value) { this.value = Objects.requireNonNull(value, "Cannot use null as error code"); }

    public String getValue() { return value; }

    @Override
    public String toString() { return getValue(); }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && Objects.equals(value, ((ErrorCode) o).value);
    }

    @Override
    public int hashCode() { return Objects.hash(value); }
}
