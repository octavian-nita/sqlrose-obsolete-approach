package eu.sqlrose.catalog;

import java.util.List;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jun 20, 2017
 */
public interface Catalog {

    interface Entry {

        String value();
    }

    List<Entry> search(String pattern);
}
