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
 * <li>is <strong>thread-safe</strong> (concurrent)</li>
 * <li>uses a {@link SoftReference}-wrapped {@link Map} instance as the backing structure to hold cached data;
 * whether or not {@code null} keys are allowed depends on this instance which can be customized by overriding
 * {@link #createBoundedCache(int)}</li>
 * </ul>
 * By default, a custom {@link LinkedHashMap} instance is used which allows {@code null} keys and implements a
 * basic <a href="https://en.wikipedia.org/wiki/Cache_replacement_policies#LRU">LRU algorithm</a>.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 2.0, Nov 7, 2017
 * @see <a href="https://www.ibm.com/developerworks/library/j-jtp01246/index.html">Plugging memory leaks with soft
 * references</a>
 * @see <a href="https://dzone.com/articles/weak-soft-and-phantom-references-in-java-and-why-they-matter">Weak, Soft,
 * and Phantom References in Java (and Why They Matter)</a>
 */
public class SoftCache<K, V> {

    protected static final int DEFAULT_MAX_SIZE = 1024;

    protected final int maxSize;

    protected final ReadWriteLock rwLock;

    protected transient SoftReference<Map<K, V>> cacheRef;

    /** Equivalent to calling '<code>new SoftCache({@link #DEFAULT_MAX_SIZE})</code>'. */
    public SoftCache() { this(DEFAULT_MAX_SIZE); }

    public SoftCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("Maximum cache size must be greater than 0");
        }
        this.maxSize = maxSize;
        this.rwLock = new ReentrantReadWriteLock();
        this.cacheRef = new SoftReference<>(createBoundedCache(this.maxSize));
    }

    public V getOrCompute(K key, Function<? super K, ? extends V> computation) {
        rwLock.writeLock().lock();
        try {
            Map<K, V> cache = cacheRef == null ? null : cacheRef.get();
            if (cache == null) {
                cacheRef = new SoftReference<>(cache = createBoundedCache(maxSize));
            }
            return cache.computeIfAbsent(key, computation);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public V get(K key) {
        rwLock.readLock().lock();
        try {
            Map<K, V> cache = cacheRef == null ? null : cacheRef.get();
            return cache == null ? null : cache.get(key);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public boolean contains(K key) {
        rwLock.readLock().lock();
        try {
            Map<K, V> cache = cacheRef == null ? null : cacheRef.get();
            return cache != null && cache.containsKey(key);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public V remove(K key) {
        rwLock.writeLock().lock();
        try {
            Map<K, V> cache = cacheRef == null ? null : cacheRef.get();
            return cache == null ? null : cache.remove(key);
        } finally {
            rwLock.writeLock().unlock();
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
        rwLock.readLock().lock();
        try {
            Map<K, V> cache = cacheRef == null ? null : cacheRef.get();
            return cache == null ? 0 : cache.size();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public int maxSize() { return maxSize; }

    public boolean isFull() {
        rwLock.readLock().lock();
        try {
            return size() == maxSize();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * Override in order to change the implementation details of the actual storage structure.
     * <p>
     * It is not required that this method be synchronized (it is not by default).
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
