package eu.sqlrose.core;

/**
 * Root of the hierarchy of SQLrose-specific exceptions.
 * <p>
 * NOTE: While there are <a href="https://www.ibm.com/developerworks/library/j-jtp05254/">good reasons</a> to make it
 * {@link Exception checked}, many (not only UI) event-based frameworks do not offer a way to propagate checked
 * exceptions out of event handlers and it's not always feasible to handle them within the handlers themselves.
 * As such one might consider using an unchecked exception hierarchy and relying on a global / default error handler.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Sep 05, 2016
 */
public class SqlRoseException extends Exception {

    private boolean internal;

    private final ErrorCode code;

    public SqlRoseException(ErrorCode code) {
        super(code == null ? null : code.value());
        this.code = code;
    }

    public SqlRoseException(ErrorCode code, Throwable cause) {
        super(code == null ? null : code.value(), cause);
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
     * @return failure-capturing pertinent data / details that contributed to the exception and
     * that might be used when resolving the {@link #getCode() code} to a localized message
     */
    public Object[] getDetails() { return code == null ? new Object[]{} : new Object[]{code}; }
}
