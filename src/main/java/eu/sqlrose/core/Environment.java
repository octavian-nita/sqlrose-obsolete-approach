package eu.sqlrose.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 11, 2016
 */
public class Environment implements Serializable {

    protected final List<ConnectionInfo> availableConnections = new ArrayList<>();

    @JsonProperty("connections")
    public List<ConnectionInfo> getAvailableConnections() { return unmodifiableList(availableConnections); }

    //
    // TODO: extract environment loading into a factory?
    //

    public Environment load(String content, String... otherContent) throws IOException {
        final ObjectReader reader = reader();

        reader.readValue(content);
        if (otherContent != null) {
            for (String c : otherContent) {
                reader.readValue(c);
            }
        }

        return this;
    }

    public Environment load(Path path, Path... otherPaths) throws IOException {
        final ObjectReader reader = reader();

        reader.readValue(path.toFile());
        if (otherPaths != null) {
            for (Path p : otherPaths) {
                reader.readValue(p.toFile());
            }
        }

        return this;
    }

    public Environment load(ClassLoader loader, String resourceName, String... otherResourceNames) throws IOException {
        final ObjectReader reader = reader();

        load(reader, loader, resourceName);
        if (otherResourceNames != null) {
            for (String n : otherResourceNames) {
                load(reader, loader, n);
            }
        }

        return this;
    }

    protected ObjectReader reader() {
        final ObjectMapper mapper = new YAMLMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return mapper.readerForUpdating(this);
    }

    protected static <T> T load(ObjectReader reader, ClassLoader loader, String resourceName) throws IOException {
        try (InputStream stream = loader.getResourceAsStream(resourceName)) {
            if (stream != null) {
                return reader.readValue(stream);
            }
        }
        return null;
    }
}
