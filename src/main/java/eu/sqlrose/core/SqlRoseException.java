package eu.sqlrose.core;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Sep 05, 2016
 */
public class SqlRoseException extends Exception {

    private boolean internal;

    private final ErrorCode code;

    public SqlRoseException(ErrorCode code) {
        super(code == null ? null : code.getCode());
        this.code = code;
    }

    public SqlRoseException(ErrorCode code, Throwable cause) {
        super(code == null ? null : code.getCode(), cause);
        this.code = code;
    }

    /** @see {@link ErrorCode} */
    public ErrorCode getCode() { return code; }

    /**
     * @return <code>true</code> if <code>this</code> exception should (preferably) not be reported to the user
     * in great detail (or even at all) but rather handled internally (logged etc.)
     */
    public boolean isInternal() { return internal; }

    public SqlRoseException setInternal(boolean internal) {
        this.internal = internal;
        return this;
    }

    /**
     * @return failure-capturing, pertinent data / details that contributed to the exception and
     * that might be used when resolving the {@link #getCode() code} to a localized message
     */
    public Object[] getDetails() { return code == null ? new Object[]{} : new Object[]{code}; }
}
