package eu.sqlrose.core;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Sep 05, 2016
 */
public class SqlRoseException extends Exception {

    private final ErrorCode code;

    private boolean internal;

    public SqlRoseException(ErrorCode code) { this.code = code; }

    public SqlRoseException(ErrorCode code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public ErrorCode getCode() { return code; }

    public boolean isInternal() { return internal; }

    public SqlRoseException setInternal(boolean internal) {
        this.internal = internal;
        return this;
    }
}
