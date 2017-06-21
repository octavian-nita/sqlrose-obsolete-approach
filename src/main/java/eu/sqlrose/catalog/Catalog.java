package eu.sqlrose.catalog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

/**
 * @param <LM> the type of Lookup Matches returned by the {@link #lookup(CharSequence)} method
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jun 20, 2017
 */
public abstract class Catalog<LM> {

    protected final Collection<CharSequence> terms;

    protected Catalog(Collection<? extends CharSequence> terms) {
        this.terms = terms == null ? new ArrayList<>() : new ArrayList<>(terms);
    }

    protected Catalog(CharSequence... terms) { this(terms == null ? null : asList(terms)); }

    protected abstract LM match(CharSequence pattern, CharSequence term);

    public List<LM> lookup(CharSequence pattern) {
        if (pattern == null) {
            return emptyList();
        }

        final List<LM> matches = new ArrayList<>();
        for (CharSequence term : terms) {
            if (term != null) {
                LM lookupMatch = match(pattern, term);
                if (lookupMatch != null) {
                    matches.add(lookupMatch);
                }
            }
        }
        return matches;
    }

    public void add(CharSequence term) {
        if (term != null) {
            this.terms.add(term);
        }
    }

    public void addAll(Collection<? extends CharSequence> terms) {
        if (terms != null) {
            for (CharSequence term : terms) {
                if (term != null) {
                    this.terms.add(term);
                }
            }
        }
    }

    public void setTerms(Collection<? extends CharSequence> terms) {
        clear();
        addAll(terms);
    }

    public void clear() { this.terms.clear(); }
}
