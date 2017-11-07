package eu.fuss4j.cache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

/**
 * A poor man's <strong>bounded</strong> cache with a <em>minimal interface</em> that:
 * <ul>
 * <li>is thread-safe (concurrent)</li>
 * <li>uses a {@link SoftReference}-wrapped {@link Map} instance as the backing structure to hold actual data;
 * whether or not {@code null} keys are allowed depends on the {@link Map} instance which can be customized by
 * overriding {@link #createBoundedCache(int)}</li>
 * </ul>
 * By default, a custom {@link LinkedHashMap} instance is used which allows {@code null} keys and implements a
 * basic <a href="https://en.wikipedia.org/wiki/Cache_replacement_policies#LRU">LRU algorithm</a>.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.1, Oct 17, 2017
 * @see <a href="https://www.ibm.com/developerworks/library/j-jtp01246/index.html">Plugging memory leaks with soft
 * references</a>
 * @see <a href="https://dzone.com/articles/weak-soft-and-phantom-references-in-java-and-why-they-matter">Weak, Soft,
 * and Phantom References in Java (and Why They Matter)</a>
 */
public class SoftCache<K, V> {

    protected static final int DEFAULT_MAX_SIZE = 1024;

    protected transient SoftReference<Map<K, V>> cacheRef;

    protected final int maxSize;

    protected final ReadWriteLock rwLock;

    /** Equivalent to calling '{@code new SoftCache({@link #DEFAULT_MAX_SIZE})}'. */
    public SoftCache() { this(DEFAULT_MAX_SIZE); }

    public SoftCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("Maximum cache size must be greater than 0");
        }
        this.maxSize = maxSize;
        this.rwLock = new ReentrantReadWriteLock();
        this.cacheRef = new SoftReference<>(createBoundedCache(this.maxSize));
    }

    public V getOrCompute(K key, Function<? super K, ? extends V> mappingFn) {
        rwLock.writeLock().lock();
        try {
            return cache().computeIfAbsent(key, mappingFn);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public V get(K key) {
        rwLock.readLock().lock();
        try {
            return cache().get(key);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public void clear() {
        rwLock.writeLock().lock();
        try {
            cacheRef = null;
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public int size() {
        Map<K, V> cache = cacheRef == null ? null : cacheRef.get();
        return cache == null ? 0 : cache.size();
    }

    public int maxSize() { return maxSize; }

    public boolean isFull() { return size() == maxSize(); }

    public boolean contains(K key) { return cache().containsKey(key); }

    /**
     * Provides convenient access to the backing {@link Map structure} that holds actual data. It is not required that
     * this method be synchronized (it is not by default).
     */
    protected Map<K, V> cache() {
        Map<K, V> cache = cacheRef == null ? null : cacheRef.get();
        if (cache == null) {
            cacheRef = new SoftReference<>(cache = createBoundedCache(maxSize));
        }
        return cache;
    }

    /**
     * Override in order to change the implementation details of the actual storage structure. It is not required that
     * this method be synchronized (it is not by default).
     *
     * @param maxSize the maximum number of elements the cache is able to store
     * @return a new instance of the backing {@link Map structure} to hold data
     */
    protected Map<K, V> createBoundedCache(int maxSize) {
        return new LinkedHashMap<K, V>(16 /* => not too many slots, initially */, 0.75f, true /* => LRU... */) {

            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) { return size() > maxSize; }
        };
    }
}
