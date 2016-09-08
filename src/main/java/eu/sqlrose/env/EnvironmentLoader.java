package eu.sqlrose.env;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.net.URL;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Sep 7, 2016
 */
class EnvironmentLoader {

    Environment load(String content, String... otherContent) { return load(null, content, otherContent); }

    Environment load(Environment target, String content, String... otherContent) {
        if (target == null) {
            target = new Environment();
        }

        final Yaml yaml = new Yaml();
        loadOne(target, content, yaml);
        if (otherContent != null) {
            for (String other : otherContent) {
                loadOne(target, other, yaml);
            }
        }

        return target;
    }

    Environment loadOne(Environment target, String content, Yaml yaml) {
        if (validateForLoad(target, content, yaml)) {
            try {
                for (Object root : yaml.loadAll(content)) {
                    load(target, root);
                }
            } catch (Throwable throwable) {
                log.error(
                    "Cannot load the environment from content " + content.substring(0, Math.min(81, content.length())) +
                    "... (see details below); ignoring the rest of the content...", throwable);
            }
        }
        return target;
    }

    Environment load(URL url, URL... otherURLs) { return load(null, url, otherURLs); }

    Environment load(Environment target, URL url, URL... otherURLs) {
        if (target == null) {
            target = new Environment();
        }

        final Yaml yaml = new Yaml();
        loadOne(target, url, yaml);
        if (otherURLs != null) {
            for (URL other : otherURLs) {
                loadOne(target, other, yaml);
            }
        }

        return target;
    }

    Environment loadOne(Environment target, URL url, Yaml yaml) {
        if (validateForLoad(target, url, yaml)) {
            try (InputStream content = url.openStream()) {

                if (content == null) {
                    log.error("Cannot load the environment from URL " + url + " (cannot open stream); ignoring...");
                } else {
                    for (Object root : yaml.loadAll(content)) {
                        load(target, root);
                    }
                }

            } catch (Throwable throwable) {
                log.error("Cannot load the environment from URL " + url +
                          " (see details below); ignoring the rest of the content...", throwable);
            }
        }
        return target;
    }

    Environment load(Environment target, Object root) {
        // TODO: implement me!
        return target;
    }

    private final Logger log = LoggerFactory.getLogger(Environment.class);

    private boolean validateForLoad(Environment target, Object source, Yaml yaml) {
        if (source == null) {
            log.warn("Cannot load the environment from a null source reference; ignoring...");
            return false;
        }

        if (target == null) {
            log.error("Cannot load the environment into a null target reference; ignoring...");
            return false;
        }
        if (yaml == null) {
            log.error("Cannot load the environment using a null Yaml reference; ignoring...");
            return false;
        }

        return true;
    }
}

//    @Override
//    public DataSource deserialize(JsonParser parser, DeserializationContext context) throws InvalidDataSource {
//        JsonNode core;
//        try {
//            core = parser.getCodec().readTree(parser);
//        } catch (IOException ioe) {
//            throw new InvalidDataSource("cannot parse the data source definition", ioe);
//        }
//
//        String name = text(core, "name");
//        if (name == null) {
//            throw new InvalidDataSource("no data source name has been specified");
//        }
//
//        String driver = text(core, "driver");
//        if (driver == null) {
//            throw new InvalidDataSource("no JDBC driver has been specified for data source '" + name +
//                                        "' and currently only driver-based data sources are supported");
//        }
//
//        String url = text(core, "url");
//        if (url != null) {
//            return new JdbcDataSource.Builder().name(name).description(text(core, "description"))
//                                                      .username(text(core, "username")).password(text(core,
// "password"))
//                                                      .driverClass(driver).url(url).properties(prop(core, "props"))
//                                                      .build();
//        }
//
//        String dbms = text(core, "dbms");
//        String dbName = text(core, "dbname");
//        if (dbms == null || dbName == null) {
//            throw new InvalidDataSource("no JDBC URL or dbms and DB name have been specified...");
//        }
//
//        return new JdbcDataSource.Builder().name(name).description(text(core, "description"))
//                                                  .username(text(core, "username")).password(text(core, "password"))
//                                                  .driverClass(driver).dbms(dbms).host(text(core, "host"))
//                                                  .port(port(core, "port")).dbName(dbName).properties(prop(core,
// "props"))
//                                                  .build();
//    }
//
//    private static int port(JsonNode root, String path) {
//        JsonNode node = node(root, path);
//        return node == null ? 0 : node.asInt(0);
//    }
//
//    private static String text(JsonNode root, String path) {
//        JsonNode node = node(root, path);
//        if (node == null) {
//            return null;
//        }
//
//        String text = node.asText();
//        return isBlank(text) ? null : text.trim();
//    }
//
//    private static Properties prop(JsonNode root, String path) {
//        JsonNode node = node(root, path);
//        if (node == null) {
//            return null;
//        }
//
//        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
//        if (fields.hasNext()) {
//            Properties props = new Properties();
//            while (fields.hasNext()) {
//                Map.Entry<String, JsonNode> field = fields.next();
//                props.put(field.getKey(), field.getValue().asText());
//            }
//            return props;
//        }
//
//        return null;
//    }
//
//    private static JsonNode node(JsonNode root, String path) {
//        if (root == null || isBlank(path)) {
//            return null;
//        }
//        JsonNode node = root.path(path);
//        return node.isNull() || node.isMissingNode() ? null : node;
//    }
