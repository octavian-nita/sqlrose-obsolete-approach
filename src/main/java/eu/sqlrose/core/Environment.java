package eu.sqlrose.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.slf4j.LoggerFactory.getLogger;

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

    public Environment loadContents(String content, String... otherContent) throws IOException {
        ObjectReader reader = mapper().readerForUpdating(this);

        load(reader, null, content);
        if (otherContent != null) {
            for (String other : otherContent) {
                load(reader, null, other);
            }
        }

        return this;
    }

    public Environment loadResources(ClassLoader loader, String resourceName, String... otherResourceNames)
        throws IOException {
        ObjectReader reader = mapper().readerForUpdating(this);

        load(reader, loader, resourceName);
        if (otherResourceNames != null) {
            for (String other : otherResourceNames) {
                load(reader, loader, other);
            }
        }

        return this;
    }

    private static ObjectMapper mapper() {
        ObjectMapper mapper = new YAMLMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(ConnectionInfo.class, new ConnectionInfoDeserializer());

        return mapper.registerModule(module);
    }

    private static <T> T load(ObjectReader reader, ClassLoader loader, String contentOrResourceName)
        throws IOException {

        if (isBlank(contentOrResourceName)) {
            getLogger(Environment.class)
                .warn("cannot load the environment from a null, empty or whitespace-only content or resource name...");
            return null;
        }

        if (loader == null) {
            return reader.readValue(contentOrResourceName);
        }

        try (InputStream input = loader.getResourceAsStream(contentOrResourceName)) {
            if (input == null) {
                getLogger(Environment.class)
                    .warn("cannot load the environment from resource named {}...", contentOrResourceName);
                return null;
            }
            return reader.readValue(input);
        }
    }
}
