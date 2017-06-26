package eu.sqlrose.lookup;

import static java.util.Objects.requireNonNull;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jun 21, 2017
 */
public class FuzzyMatch {

    private final CharSequence term;

    public FuzzyMatch(CharSequence term) {
        this.term = requireNonNull(term);
    }

    public CharSequence getTerm() { return term; }
}
