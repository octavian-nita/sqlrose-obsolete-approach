package eu.sqlrose.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final Logger log = LoggerFactory.getLogger(Environment.class);

    protected final List<ConnectionInfo> availableConnections = new ArrayList<>();

    public List<ConnectionInfo> getAvailableConnections() { return unmodifiableList(availableConnections); }

    public Environment loadConnections(Path path, Path... otherPaths) {
        loadConnectionsFrom(path);
        if (otherPaths != null) {
            for (Path onePath : otherPaths) {
                loadConnectionsFrom(onePath);
            }
        }
        return this;
    }

    protected void loadConnectionsFrom(Path path) {
        throw new RuntimeException("Not yet implemented");
    }

    public Environment loadConnections(String content, String... otherContent) {
        loadConnectionsFrom(content);
        if (otherContent != null) {
            for (String oneContent : otherContent) {
                loadConnectionsFrom(oneContent);
            }
        }
        return this;
    }

    protected void loadConnectionsFrom(String content) {
        throw new RuntimeException("Not yet implemented");
    }
}
