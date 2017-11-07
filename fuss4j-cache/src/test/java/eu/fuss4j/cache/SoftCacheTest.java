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
        computation = mock(Function.class);
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
        cacheUnderTest.getOrCompute(arg, computation);

        verify(computation, description("getOrCompute() should invoke the computation once")).apply(arg);
        assertEquals("Cache should store 1 entry", 1, cacheUnderTest.size());
        assertEquals("Cache should store computed value", res, cacheUnderTest.get(arg));
    }

    @Test
    public void getOrCompute_KeyTwiceGiven_ShouldComputeOnlyOnce() {
        final String arg = "arg";
        final String res = "res";

        when(computation.apply(arg)).thenReturn(res);
        cacheUnderTest.getOrCompute(arg, computation);
        cacheUnderTest.getOrCompute(arg, computation);

        verify(computation, description("getOrCompute() should invoke the computation only once")).apply(arg);
        assertEquals("Cache should store 1 entry", 1, cacheUnderTest.size());
        assertEquals("Cache should store and retrieve computed value", res, cacheUnderTest.get(arg));
    }

    @Test
    public void getOrCompute_NewKeyGivenWhenFull_ShouldRemoveLRUEntry() {

        // Fixture

        String arg0 = "", argNMin1 = "", resNMin1 = "", argN = "", resN = "";
        for (int i = 0; i <= setUpBound; i++) {

            String arg = "arg-" + i, res = "res-" + i;
            when(computation.apply(arg)).thenReturn(res);

            switch (i) {
            case 0:
                arg0 = arg;
                break;
            case setUpBound - 1:
                argNMin1 = arg;
                resNMin1 = res;
                break;
            case setUpBound:
                argN = arg;
                resN = res;
                break;
            }
        }

        // Exercise

        for (int i = 0; i <= setUpBound; i++) {
            cacheUnderTest.getOrCompute("arg-" + i, computation);
        }

        // Assert / Verify

        assertEquals("Cache should store " + setUpBound + " entries", setUpBound, cacheUnderTest.size());
        assertTrue("Cache should report as being full", cacheUnderTest.isFull());

        assertFalse("Cache should NOT contain first key given", cacheUnderTest.contains(arg0));
        assertNull("Cache should NOT store first computed value", cacheUnderTest.get(arg0));

        assertTrue("Cache should contain key N-1", cacheUnderTest.contains(argNMin1));
        assertEquals("Cache should store value N-1", resNMin1, cacheUnderTest.get(argNMin1));

        assertTrue("Cache should contain last key given", cacheUnderTest.contains(argN));
        assertEquals("Cache should store last computed value", resN, cacheUnderTest.get(argN));
    }
}
