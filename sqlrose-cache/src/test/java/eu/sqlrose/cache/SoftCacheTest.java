package eu.sqlrose.cache;

import org.junit.*;

import java.util.function.Function;

import static org.junit.Assert.*;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Oct 17, 2017
 */
public class SoftCacheTest {

    protected SoftCache<String, String> cacheUnderTest;

    protected CountingSuffix computation;

    protected final int setUpBound = 2;

    @Before
    public void setUp() {
        cacheUnderTest = new SoftCache<>(setUpBound);
        computation = new CountingSuffix(" => value");
    }

    @After
    public void tearDown() {
        cacheUnderTest = null;
        computation = null;
    }

    @Test
    public void newCache_BoundedAtN_SizeIs0AndMaxSizeIsN() {
        assertEquals("Cache should store 0 initial entries (size)", 0, cacheUnderTest.size());
        assertEquals("Cache should have 2 available entries (max size)", setUpBound, cacheUnderTest.maxSize());
    }

    @Test
    public void getOrCompute_NewKeyGiven_ShouldComputeAndStoreValue() {
        final String key = "key #1";
        final String value = computation.apply(key);
        final String valueCached = cacheUnderTest.getOrCompute(key, computation);

        assertEquals("getOrCompute() should invoke the computation once", 2, computation.getCallCount());
        assertEquals("Cache should store 1 entry", 1, cacheUnderTest.size());
        assertEquals("Cache should store computed value", value, valueCached);
    }

    @Test
    public void getOrCompute_KeyTwiceGiven_ShouldNotComputeTwice() {
        final String key = "key #1";
        final String value = computation.apply(key);
        final String valueCached = cacheUnderTest.getOrCompute(key, computation);
        final String valueFromCache = cacheUnderTest.getOrCompute(key, computation);

        assertEquals("getOrCompute() should invoke the computation only once", 2, computation.getCallCount());
        assertEquals("Cache should store 1 entry", 1, cacheUnderTest.size());
        assertEquals("Cache should store computed value", value, valueCached);
        assertEquals("Cache should retrieve computed value", value, valueFromCache);
    }

    @Test
    public void getOrCompute_NewKeyGivenWhenFull_ShouldRemoveLRUEntry() {
        final String key1 = "key #1";
        final String key2 = "key #2";
        final String key3 = "key #3";
        final String value1 = computation.apply(key1);
        final String value2 = computation.apply(key2);
        final String value3 = computation.apply(key3);

        final String value1Cached = cacheUnderTest.getOrCompute(key1, computation);
        final String value2Cached = cacheUnderTest.getOrCompute(key2, computation);

        assertEquals("Cache should store 2 entry", 2, cacheUnderTest.size());
        assertTrue("Cache should report as being full", cacheUnderTest.isFull());

        assertTrue("Cache should contain given key", cacheUnderTest.contains(key1));
        assertEquals("Cache should store computed value", value1, value1Cached);

        assertTrue("Cache should contain given key", cacheUnderTest.contains(key2));
        assertEquals("Cache should store computed value", value2, value2Cached);

        final String value3Cached = cacheUnderTest.getOrCompute(key3, computation);

        assertEquals("Cache should still store 2 entry", 2, cacheUnderTest.size());
        assertTrue("Cache should still report as being full", cacheUnderTest.isFull());

        assertTrue("Cache should contain the latest given key", cacheUnderTest.contains(key3));
        assertEquals("Cache should store the latest computed value", value3, value3Cached);

        assertFalse("Cache should have removed the LRU / eldest entry", cacheUnderTest.contains(key1));
        assertEquals("getOrCompute() should invoke the computation for new keys", 6, computation.getCallCount());
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
