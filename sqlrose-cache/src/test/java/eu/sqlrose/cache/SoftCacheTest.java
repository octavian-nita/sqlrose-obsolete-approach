package eu.sqlrose.cache;

import org.junit.*;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;

/**
 * TODO http://www.vogella.com/tutorials/Mockito/article.html
 * TODO https://zeroturnaround.com/rebellabs/how-to-mock-up-your-unit-test-environment-to-create-alternate-realities/
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Oct 17, 2017
 */
public class SoftCacheTest {

    protected SoftCache<String, String> cacheUnderTest;

    @Before
    public void setUp() { cacheUnderTest = new SoftCache<>(2); }

    @After
    public void tearDown() { cacheUnderTest = null; }

    @Test
    public void testCacheInitialState() {
        assertEquals("Cache should store 0 initial entries (size)", 0, cacheUnderTest.size());
        assertEquals("Cache should have 2 available entries (max size)", 2, cacheUnderTest.maxSize());
    }

    @Test
    public void testCacheStateAfterInitialMiss() {

        final CountingSuffix computation = new CountingSuffix(" => value #1");

        final String firstKey = "key #1";
        final String firstVal = cacheUnderTest.getOrCompute(firstKey, computation);
        final String firstRec = computation.apply(firstKey);

        assertEquals("Cache should store 1 entry (size)", 1, cacheUnderTest.size());
        assertEquals("Cache should store computed value", firstRec, firstVal);
        assertEquals("Cache should not invoke computation twice", 2, computation.getCallCount());
    }

    private static final class CountingSuffix implements Function<String, String> {

        private int callCount;

        private final Object suffix;

        CountingSuffix(Object suffix) { this.suffix = suffix; }

        int getCallCount() { return callCount; }

        @Override
        public String apply(String stem) {
            try {
                System.out.println("Applying suffix '" + suffix + "' to stem '" + stem + "'...");
                return stem + suffix;
            } finally {
                callCount++;
            }
        }
    }
}
