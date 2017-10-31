package eu.fuss4j.cache.fixture;

import java.util.function.Function;

/**
 * Primitive (as in 'basic') <a href="https://en.wikipedia.org/wiki/Mock_object">mock object</a>, implementing a
 * {@link Function} that adds a {@link #CallCountingSuffix(Object) predefined suffix} to its argument and counts
 * and {@link #getCallCount() exposes} the number of times it has been called.
 * <p/>
 * However, the recommended way to (unit) test things is to use a proper mocking framework like
 * <a href="http://site.mockito.org/">Mockito</a>, etc.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Oct 17, 2017
 * @see
 * <a href="https://zeroturnaround.com/rebellabs/how-to-mock-up-your-unit-test-environment-to-create-alternate-realities/">How to mock up your Unit Test environment</a>
 */
public class CallCountingSuffix implements Function<String, String> {

    private int callCount;

    private final Object suffix;

    public int getCallCount() { return callCount; }

    public CallCountingSuffix(Object suffix) { this.suffix = suffix; }

    @Override
    public String apply(String stem) {
        callCount++;
        return stem + suffix;
    }
}
