package eu.fuss4j.cache;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Sep 27, 2017
 */
public final class MemoizedBiFn<T, U, R> implements BiFunction<T, U, R> {

    private final BiFunction<T, U, R> delegate;

    private final SoftCache<Pair<T, U>, R> cache;

    public MemoizedBiFn(BiFunction<T, U, R> delegate) { this(delegate, SoftCache.DEFAULT_MAX_SIZE); }

    public MemoizedBiFn(BiFunction<T, U, R> delegate, int maxMemoized) {
        this.delegate = Objects.requireNonNull(delegate, "Cannot memoize a null bi-function");
        this.cache = new SoftCache<>(maxMemoized);
    }

    public void clearCache() { cache.clear(); }

    @Override
    public R apply(T t, U u) { return cache.getOrCompute(Pair.of(t, u), tAndU -> delegate.apply(t, u)); }

    public static <T, U, R> MemoizedBiFn<T, U, R> memoize(BiFunction<T, U, R> fn) { return new MemoizedBiFn<>(fn); }

    public static <T, U, R> MemoizedBiFn<T, U, R> memoizeAtMost(BiFunction<T, U, R> fn, int maxMemoized) {
        return new MemoizedBiFn<>(fn, maxMemoized);
    }

    private static final class Pair<F, S> {

        private final F first;

        private final S second;

        private final int hash; // a Pair instance is immutable hence we can cache the hash

        private Pair(F first, S second) {
            this.first = first;
            this.second = second;
            this.hash = Objects.hash(first, second);
        }

        private static <F, S> Pair<F, S> of(F first, S second) { return new Pair<>(first, second); }

        @Override
        public String toString() { return "(" + first + ", " + second + ")"; }

        @Override
        public boolean equals(Object o) {
            return this == o ||
                   o != null && getClass() == o.getClass() && Objects.equals(first, ((Pair<?, ?>) o).first) &&
                   Objects.equals(second, ((Pair<?, ?>) o).second);
        }

        @Override
        public int hashCode() { return hash; }
    }
}
