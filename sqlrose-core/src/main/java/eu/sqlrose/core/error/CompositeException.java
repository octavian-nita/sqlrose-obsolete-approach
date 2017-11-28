package eu.sqlrose.core.error;

import java.util.*;

import static eu.sqlrose.core.error.ErrorCode.E_COMPOSITE;
import static eu.sqlrose.log.Log.w;
import static java.lang.System.arraycopy;
import static java.util.Collections.unmodifiableList;

/**
 * An exception that is a composite of one or more other exceptions.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Oct 06, 2016
 * @see <a href="http://baddotrobot.com/blog/2011/08/29/catching-multiple-exceptions-and/">
 * Catching Multiple Exceptions (and rethrowing them all!)</a>
 * @see
 * <a href="https://github.com/ReactiveX/RxJava/blob/2.x/src/main/java/io/reactivex/exceptions/CompositeException.java">
 * RxJava's <code>CompositeException</code></a>
 */
public final class CompositeException extends SqlRoseException implements Iterable<Throwable> {

    private final List<Throwable> exceptions = new ArrayList<>();

    public CompositeException(Throwable... exceptions) { this(exceptions == null ? null : Arrays.asList(exceptions)); }

    public CompositeException(Iterable<? extends Throwable> exceptions) {
        super(E_COMPOSITE);

        if (exceptions != null) {
            for (Throwable exception : exceptions) {
                add(exception);
            }
        }
    }

    @Override
    public Iterator<Throwable> iterator() { return exceptions.iterator(); }

    @Override
    public Object[] getDetails() {
        final Object[] superDetails = super.getDetails();
        if (superDetails == null || superDetails.length == 0) {
            return new Object[]{getCode(), size()};
        }

        final Object[] details = new Object[superDetails.length + 1];
        arraycopy(superDetails, 0, details, 0, details.length);
        details[details.length - 1] = size();
        return details;
    }

    public int size() { return exceptions.size(); }

    public Throwable getException(int index) { return exceptions.get(index); }

    public List<Throwable> getExceptions() { return unmodifiableList(exceptions); }

    public CompositeException add(Throwable exception) {
        if (exception == null) {
            this.exceptions.add(new NullPointerException("null exception was provided to the composite"));
        } else if (exception instanceof CompositeException) {
            this.exceptions.addAll(((CompositeException) exception).exceptions);
        } else {
            this.exceptions.add(exception);
        }

        return this;
    }

    public void throwIfNotEmpty() throws CompositeException {
        if (!exceptions.isEmpty()) {
            throw this;
        } else {
            w("no exception was provided to the composite");
        }
    }
}
