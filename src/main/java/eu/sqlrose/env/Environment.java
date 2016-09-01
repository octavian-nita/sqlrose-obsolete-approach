package eu.sqlrose.env;

import eu.sqlrose.core.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.unmodifiableCollection;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * The {@link #load(String, String...) loading} {@link #load(URL, URL...) capabilities} provided by default never throw
 * any exceptions; in case of invalid input or any other loading error, they log and do not modify the currently loaded
 * environment (i.e. <code>this</code>).
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 11, 2016
 */
public class Environment implements Serializable {

    private final Map<String, DataSource> dataSources = new LinkedHashMap<>();

    public void setDataSources(Collection<? extends DataSource> dataSources) {
        this.dataSources.clear();
        if (dataSources != null) {
            dataSources.stream().filter(dataSource -> dataSource != null)
                       .forEach(dataSource -> this.dataSources.put(dataSource.getName(), dataSource));
        }
    }

    public Collection<DataSource> getDataSources() { return unmodifiableCollection(dataSources.values()); }

    public DataSource getDataSource(String name) { return dataSources.get(name); }

    public DataSource add(DataSource dataSource) {
        return dataSources
            .put(notNull(dataSource, "cannot add a null data source to the environment").getName(), dataSource);
    }

    public DataSource remove(DataSource dataSource) {
        return dataSources
            .remove(notNull(dataSource, "cannot remove a null data source from the environment").getName());
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
//        ObjectReader reader = mapper().readerForUpdating(this);
//
//        loadOne(reader, url);
//        if (otherURLs != null) {
//            for (URL other : otherURLs) {
//                loadOne(reader, other);
//            }
//        }
//
        return this;
    }

//    protected ObjectMapper mapper() {
//        ObjectMapper mapper = new YAMLMapper();
//        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
//        return mapper
//            .registerModule(new SimpleModule().addDeserializer(DataSource.class, new DataSourceDeserializer()));
//    }

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
//            log.error("cannot load the environment string '" + content.substring(0, Math.min(content.length(), 51)) +
//                      "...' (see details below); skip loading...", throwable);
//            return Optional.empty();
//        }
        return Optional.empty();
    }

    protected <T> Optional<T> loadOne(/*ObjectReader reader,*/ URL url) {
//        if (reader == null) {
//            log.debug("no reader to load the environment from URL has been provided; skip loading...");
//            return Optional.empty();
//        }
//
//        if (url == null) {
//            log.warn("cannot load the environment from a null URL; skip loading...");
//            return Optional.empty();
//        }
//
//        try {
//            return Optional.ofNullable(reader.readValue(url));
//        } catch (Throwable throwable) {
//            log.error("cannot load the environment from URL " + url + " (see details below); skip loading...",
//                      throwable);
//            return Optional.empty();
//        }
        return Optional.empty();
    }

    protected final Logger log = LoggerFactory.getLogger(Environment.class);
}
