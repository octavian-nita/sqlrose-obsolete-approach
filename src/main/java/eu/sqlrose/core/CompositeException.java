package eu.sqlrose.core;

import java.util.*;

/**
 * An exception that is a composite of one or more other exceptions.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Oct 06, 2016
 * @see
 * <a href="https://github.com/ReactiveX/RxJava/blob/2.x/src/main/java/io/reactivex/exceptions/CompositeException.java">
 * RxJava's <code>CompositeException</code></a>
 * @see <a href="http://baddotrobot.com/blog/2011/08/29/catching-multiple-exceptions-and/">
 * Catching Multiple Exceptions (and rethrowing them all!)</a>
 */
public final class CompositeException extends SqlRoseException implements Iterable<Throwable> {

    private final List<Throwable> exceptions = new ArrayList<>();

    public CompositeException(Throwable... exceptions) { this(exceptions == null ? null : Arrays.asList(exceptions)); }

    public CompositeException(Iterable<? extends Throwable> exceptions) {
        super(ErrorCode.E_COMPOSITE);

        if (exceptions == null) {
            this.exceptions.add(new NullPointerException("exceptions was null"));
        } else {
            for (Throwable exception : exceptions) {
                if (exception == null) {
                    this.exceptions.add(new NullPointerException("throwable was null"));
                } else if (exception instanceof CompositeException) {
                    this.exceptions.addAll(((CompositeException) exception).exceptions);
                } else {
                    this.exceptions.add(exception);
                }
            }
        }
    }

    @Override
    public Iterator<Throwable> iterator() { return exceptions.iterator(); }

    @Override
    public Object[] getDetails() { return new Integer[]{size()}; }

    public int size() { return exceptions.size(); }

    public Throwable getException(int index) { return exceptions.get(index); }

    public List<Throwable> getExceptions() { return Collections.unmodifiableList(exceptions); }

    public CompositeException add(Throwable exception) {
        if (exception == null) {
            this.exceptions.add(new NullPointerException("throwable was null"));
        } else if (exception instanceof CompositeException) {
            this.exceptions.addAll(((CompositeException) exception).exceptions);
        } else {
            this.exceptions.add(exception);
        }

        return this;
    }

    public void checkAndThrow() throws CompositeException {
        if (!exceptions.isEmpty()) {
            throw this;
        }
    }
}
