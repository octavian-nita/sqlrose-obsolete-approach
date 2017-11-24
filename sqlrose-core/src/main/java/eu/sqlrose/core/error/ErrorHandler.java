package eu.sqlrose.core.error;

import static eu.sqlrose.log.Log.e;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.setDefaultUncaughtExceptionHandler;

/**
 * A strategy for handling errors, modelled after
 * <a href="https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/util/ErrorHandler.html">Spring's
 * ErrorHandler</a>.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 2.0, Nov 24, 2017
 * @see
 * <a href="https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/util/ErrorHandler.html">Spring's
 * ErrorHandler</a>
 */
@FunctionalInterface
public interface ErrorHandler extends Thread.UncaughtExceptionHandler {

    /** Handles {@code throwable} in a sufficient way (i.e. no further throwing ). */
    void handle(Throwable throwable);

    /** {@inheritDoc} */
    @Override
    default void uncaughtException(Thread thread, Throwable throwable) {
        e(thread == null ? "[null thread]" : thread.toString());
        handle(throwable);
    }

    /**
     * Installs <code>this</code> error handler as the {@link
     * Thread#setDefaultUncaughtExceptionHandler(Thread.UncaughtExceptionHandler) default uncaught exception handler}.
     */
    default ErrorHandler asDefaultUncaughtExceptionHandler() {
        setDefaultUncaughtExceptionHandler(this);
        return this;
    }

    /**
     * Installs <code>this</code> error handler as the {@link
     * Thread#setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler) uncaught exception handler} for the
     * {@link Thread#currentThread() current thread}.
     */
    default ErrorHandler asUncaughtExceptionHandler() {
        currentThread().setUncaughtExceptionHandler(this);
        return this;
    }

    /**
     * Common error handling logic, like {@link eu.sqlrose.log.Log logging}, etc.
     */
    class Base implements ErrorHandler {

        /** TODO extend with custom handling for SqlRoseException, CompositeException, etc. */
        @Override
        public void handle(Throwable throwable) { e(throwable); }
    }

    /** Many times, basic error handling suffices. */
    ErrorHandler BASIC = new Base();
}
