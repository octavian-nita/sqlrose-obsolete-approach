package eu.fuss4j.cache;

import org.junit.*;

import java.util.function.Function;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Oct 17, 2017
 */
public class SoftCacheTest {

    protected SoftCache<String, String> cacheUnderTest;

    protected Function<String, String> computation;

    protected final int setUpBound = 2;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        cacheUnderTest = new SoftCache<>(setUpBound);
        computation = mock(Function.class, RETURNS_DEEP_STUBS);
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
        final String arg = "arg";
        final String res = "res";

        when(computation.apply(arg)).thenReturn(res);
        final String cachedRes = cacheUnderTest.getOrCompute(arg, computation);

        verify(computation, description("getOrCompute() should invoke the computation once")).apply(arg);
        assertEquals("Cache should store 1 entry", 1, cacheUnderTest.size());
        assertEquals("Cache should store computed value", res, cachedRes);
    }

    @Test
    public void getOrCompute_KeyTwiceGiven_ShouldNotComputeTwice() {
        final String arg = "arg";
        final String res = "res";

        when(computation.apply(arg)).thenReturn(res);
        final String cachedRes1 = cacheUnderTest.getOrCompute(arg, computation);
        final String cachedRes2 = cacheUnderTest.getOrCompute(arg, computation);

        verify(computation, description("getOrCompute() should invoke the computation only once")).apply(arg);
        assertEquals("Cache should store 1 entry", 1, cacheUnderTest.size());
        assertEquals("Cache should store computed value", res, cachedRes1);
        assertEquals("Cache should retrieve computed value", res, cachedRes2);
    }

    @Test
    public void getOrCompute_NewKeyGivenWhenFull_ShouldRemoveLRUEntry() {
        /*final String key1 = "key #1";
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
        assertEquals("getOrCompute() should invoke the computation for new keys", 6, computation.getCallCount());*/
    }
}
