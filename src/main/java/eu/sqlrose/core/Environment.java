package eu.sqlrose.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.unmodifiableCollection;
import static org.apache.commons.lang3.StringUtils.isBlank;
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

    private final Map<String, ConnectionInfo> connections = new LinkedHashMap<>();

    public void setConnections(Collection<? extends ConnectionInfo> connections) {
        this.connections.clear();
        if (connections != null) {
            for (ConnectionInfo connection : connections) {
                if (connection != null) {
                    this.connections.put(connection.getName(), connection);
                }
            }
        }
    }

    public Collection<ConnectionInfo> getConnections() { return unmodifiableCollection(connections.values()); }

    public ConnectionInfo getConnection(String name) { return connections.get(name); }

    public ConnectionInfo add(ConnectionInfo connection) {
        return connections
            .put(notNull(connection, "cannot add a null connection to the environment").getName(), connection);
    }

    public ConnectionInfo remove(ConnectionInfo connection) {
        return connections
            .remove(notNull(connection, "cannot remove a null connection from the environment").getName());
    }

    //
    // Environment loading
    //

    public Environment load(String content, String... otherContent) {
        ObjectReader reader = mapper().readerForUpdating(this);

        loadOne(reader, content);
        if (otherContent != null) {
            for (String other : otherContent) {
                loadOne(reader, other);
            }
        }

        return this;
    }

    public Environment load(URL url, URL... otherURLs) {
        ObjectReader reader = mapper().readerForUpdating(this);

        loadOne(reader, url);
        if (otherURLs != null) {
            for (URL other : otherURLs) {
                loadOne(reader, other);
            }
        }

        return this;
    }

    protected ObjectMapper mapper() {
        ObjectMapper mapper = new YAMLMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return mapper
            .registerModule(new SimpleModule().addDeserializer(ConnectionInfo.class, new ConnectionInfoDeserializer()));
    }

    protected <T> Optional<T> loadOne(ObjectReader reader, String content) {
        if (reader == null) {
            log.debug("no reader to load the environment string has been provided; skip loading...");
            return Optional.empty();
        }

        if (isBlank(content)) {
            log.warn("cannot load a null, empty or whitespace-only environment string; skip loading...");
            return Optional.empty();
        }

        try {
            return Optional.ofNullable(reader.readValue(content));
        } catch (Throwable throwable) {
            log.error("cannot load the environment string '" + content.substring(0, Math.min(content.length(), 51)) +
                      "...' (see details below); skip loading...", throwable);
            return Optional.empty();
        }
    }

    protected <T> Optional<T> loadOne(ObjectReader reader, URL url) {
        if (reader == null) {
            log.debug("no reader to load the environment from URL has been provided; skip loading...");
            return Optional.empty();
        }

        if (url == null) {
            log.warn("cannot load the environment from a null URL; skip loading...");
            return Optional.empty();
        }

        try {
            return Optional.ofNullable(reader.readValue(url));
        } catch (Throwable throwable) {
            log.error("cannot load the environment from URL " + url + " (see details below); skip loading...",
                      throwable);
            return Optional.empty();
        }
    }

    protected final Logger log = LoggerFactory.getLogger(Environment.class);
}
