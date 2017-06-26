package eu.sqlrose.lookup;

import java.util.*;

import static java.util.Collections.unmodifiableSet;
import static java.util.Optional.ofNullable;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jun 26, 2017
 */
public class Match<LI extends LookupItem> {

    private final LI item;

    private final int score;

    private final Set<Location> locations;

    public Match(LI item, int score, Collection<Location> locations) {
        if (item == null) {
            throw new NullPointerException("Matched item cannot be null");
        }

        this.item = item;
        this.score = score;
        this.locations = locations == null ? null : unmodifiableSet(new HashSet<>(locations));
    }

    public LI getItem() { return item; }

    public int getScore() { return score; }

    public static final Comparator<Match> BY_SCORE = (m1, m2) -> {
        if (m1 == null || m2 == null) {
            throw new NullPointerException("Cannot compare null matches");
        }
        return m1.getScore() - m2.getScore();
    };

    public Optional<Set<Location>> getLocations() { return ofNullable(locations); }

    public static class Location {

        public final int start;

        public final int end;

        /**
         * @param start the start index within the {@link #item matched item's}
         *              {@link LookupItem#getCharSequence() char sequence representation} where ({@code end - start})
         *              characters in a pattern are found sequentially; inclusive
         * @param end   the end index within the {@link #item matched item's}
         *              {@link LookupItem#getCharSequence() char sequence representation} where characters in a
         *              pattern do not match anymore; exclusive
         * @throws IndexOutOfBoundsException if {@code start} is negative or if {@code start} is greater than or
         *                                   equal to {@code end}
         */
        public Location(int start, int end) {
            if (start < 0) {
                throw new IndexOutOfBoundsException("Char sequence start index out of range: " + start);
            }
            if (start >= end) {
                throw new IndexOutOfBoundsException(
                    "Char sequence end index lower than or equal to start index: " + end);
            }
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o != null && getClass() == o.getClass() && start == ((Location) o).start &&
                                end == ((Location) o).end;
        }

        @Override
        public int hashCode() { return (17 * 37 + start) * 37 + end; }
    }
}
