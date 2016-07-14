package eu.sqlrose.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 11, 2016
 */
public class Environment implements Serializable {

    protected final Set<ConnectionInfo> connections = new LinkedHashSet<>();

    public void setConnections(Collection<? extends ConnectionInfo> connections) {
        this.connections.clear();
        if (connections != null) {
            for (ConnectionInfo connection : connections) {
                if (connection != null) {
                    this.connections.add(connection);
                }
            }
        }
    }

    public Set<ConnectionInfo> getConnections() { return unmodifiableSet(connections); }

    public boolean add(ConnectionInfo connection) { return connections.add(connection); }

    public boolean remove(ConnectionInfo connection) { return connections.remove(connection); }

    //
    // Environment Loading
    //

    public Environment load(String content, String... otherContent) throws IOException {
        ObjectReader reader = mapper().readerForUpdating(this);

        reader.readValue(notBlank(content, "the environment string cannot be null, empty or whitespace-only"));
        if (otherContent != null) {
            for (String other : otherContent) {
                reader.readValue(notBlank(other, "the environment string cannot be null, empty or whitespace-only"));
            }
        }

        return this;
    }

    public Environment load(URL url, URL... otherURLs) throws IOException {
        ObjectReader reader = mapper().readerForUpdating(this);

        reader.readValue(notNull(url, "the environment URL cannot be null"));
        if (otherURLs != null) {
            for (URL other : otherURLs) {
                reader.readValue(notNull(other, "the environment URL cannot be null"));
            }
        }

        return this;
    }

    private static ObjectMapper mapper() {
        ObjectMapper mapper = new YAMLMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return mapper
            .registerModule(new SimpleModule().addDeserializer(ConnectionInfo.class, new ConnectionInfoDeserializer()));
    }
}
