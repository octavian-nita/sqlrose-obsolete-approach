package eu.sqlrose.core;

import java.io.Serializable;

/**
 * Supports separating user-level error messages from {@link SqlRoseException exception} code. When it's
 * time to display the exception, the {@link #getCode() code} can be resolved to a localized message.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Sep 05, 2016
 */
public interface ErrorCode extends Serializable {

    String getCode();

    ErrorCode E_GENERIC = (ErrorCode) () -> "E_GENERIC";
}
