package eu.sqlrose.lookup;

/**
 * A {@link LookupCatalog catalog} item that can be looked up based on its
 * {@link #getCharSequence() representation as a sequence of char values}.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jun 26, 2017
 */
public interface LookupItem {

    /**
     * @return {@code this} item's representation as a sequence of characters, that can be the input of a lookup
     * algorithm or service
     */
    CharSequence getCharSequence();
}
