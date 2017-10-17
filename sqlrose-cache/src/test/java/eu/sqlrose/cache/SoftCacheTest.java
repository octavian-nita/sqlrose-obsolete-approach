package eu.sqlrose.cache;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Oct 17, 2017
 */
public class SoftCacheTest {

    public void testCachingWorks() {
        final SoftCache<String, String> cache = new SoftCache<>(2);
    }
}
