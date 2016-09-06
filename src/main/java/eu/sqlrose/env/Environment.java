package eu.sqlrose.env;

import eu.sqlrose.core.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.unmodifiableCollection;

/**
 * The {@link #load(String, String...) loading} {@link #load(URL, URL...) capabilities} provided by default never throw
 * any exceptions; in case of invalid input or any other loading error they log and do not modify the currently loading
 * environment (i.e. <code>this</code>) with the erroneous entry.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 11, 2016
 */
public class Environment implements Serializable {

    protected final Logger log = LoggerFactory.getLogger(Environment.class);

    private final Map<String, DataSource> dataSources = new LinkedHashMap<>();

    public void setDataSources(Collection<? extends DataSource> dataSources) {
        this.dataSources.clear();
        if (dataSources != null) {
            dataSources.stream().filter(ds -> ds != null).forEach(ds -> this.dataSources.put(ds.getName(), ds));
        }
    }

    public Collection<DataSource> getDataSources() { return unmodifiableCollection(dataSources.values()); }

    public DataSource getDataSource(String name) { return dataSources.get(name); }

    public DataSource add(DataSource dataSource) {
        if (dataSource == null) {
            log.error("Cannot add a null data source to the environment; ignoring...");
            return null;
        } else {
            return dataSources.put(dataSource.getName(), dataSource);
        }
    }

    public DataSource remove(DataSource dataSource) {
        return dataSource == null ? null : dataSources.remove(dataSource.getName());
    }

    //
    // Environment loading
    //

    public Environment load(String content, String... otherContent) {
        //        ObjectReader reader = mapper().readerForUpdating(this);
        //
        //        loadOne(reader, content);
        //        if (otherContent != null) {
        //            for (String other : otherContent) {
        //                loadOne(reader, other);
        //            }
        //        }
        //
        return this;
    }

    public Environment load(URL url, URL... otherURLs) {
        final Yaml yaml = new Yaml();

        loadOne(yaml, url);
        if (otherURLs != null) {
            for (URL other : otherURLs) {
                loadOne(yaml, other);
            }
        }

        return this;
    }

    protected <T> Optional<T> loadOne(/*ObjectReader reader,*/ String content) {
        //        if (reader == null) {
        //            log.debug("no reader to load the environment string has been provided; skip loading...");
        //            return Optional.empty();
        //        }
        //
        //        if (isBlank(content)) {
        //            log.warn("cannot load a null, empty or whitespace-only environment string; skip loading...");
        //            return Optional.empty();
        //        }
        //
        //        try {
        //            return Optional.ofNullable(reader.readValue(content));
        //        } catch (Throwable throwable) {
        //            log.error("cannot load the environment string '" + content.substring(0, Math.min(content.length
        // (), 51)) +
        //                      "...' (see details below); skip loading...", throwable);
        //            return Optional.empty();
        //        }
        return Optional.empty();
    }

    protected Environment loadOne(Yaml yaml, URL url) {
        if (yaml == null) {
            log.debug("No YAML instance has been provided to load the environment from a URL; ignoring...");
            return this;
        }

        if (url == null) {
            log.warn("Cannot load the environment from a null URL; ignoring...");
            return this;
        }

        try {
            yaml.loadAll(url.openStream());
        } catch (Throwable throwable) {
            log.error("Cannot load the environment from URL " + url + " (see details below); ignoring...", throwable);
        }
        return this;
    }
}
