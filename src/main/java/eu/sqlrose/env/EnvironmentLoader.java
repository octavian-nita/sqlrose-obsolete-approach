package eu.sqlrose.env;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

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

    private final Logger log = LoggerFactory.getLogger(Environment.class);

    private void loadOne(Environment target, String content, Yaml yaml) {
        if (validateForLoad(target, content, yaml)) {
            try {
                for (Object root : yaml.loadAll(content)) {
                    loadRoot(target, root);
                }
            } catch (Throwable throwable) {
                log.error(
                    "Cannot load the environment from content " + content.substring(0, Math.min(81, content.length())) +
                    "... (see details below); ignoring the rest of the content...", throwable);
            }
        }
    }

    private void loadOne(Environment target, URL url, Yaml yaml) {
        if (validateForLoad(target, url, yaml)) {
            try (InputStream content = url.openStream()) {

                if (content == null) {
                    log.error("Cannot load the environment from URL " + url + " (cannot open stream); ignoring...");
                } else {
                    for (Object root : yaml.loadAll(content)) {
                        loadRoot(target, root);
                    }
                }

            } catch (Throwable throwable) {
                log.error("Cannot load the environment from URL " + url +
                          " (see details below); ignoring the rest of the content...", throwable);
            }
        }
    }

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

    private void loadRoot(Environment target, Object root) {
        if (target != null && root != null) {
            if (root instanceof Map) {

                // Data sources:

                Object dsRoot = ((Map) root).get(DSS_KEY);
                if (dsRoot != null) {
                    loadDataSources(target, dsRoot);
                }
                dsRoot = ((Map) root).get(DS_KEY);
                if (dsRoot != null) {
                    loadDataSources(target, dsRoot);
                }

            }
        }
    }

    private static final String DSS_KEY = "data_sources";

    private static final String DS_KEY = "data_source";

    private void loadDataSources(Environment target, Object dsRoot) {
        if (dsRoot instanceof Collection) {
            for (Object ds : (Collection) dsRoot) {
                loadDataSources(target, ds);
            }
            return;
        }

        if (dsRoot instanceof Map) {
            // TODO: implement me
            return;
        }

        log.error("Cannot load data source(s) from " + dsRoot + "; ignoring...");
    }

    private String text(Map<?, ?> map, Object key) {
        Object val = map == null ? null : map.get(key);
        return val == null ? null : val.toString().trim();
    }

    private int port(Map<?, ?> map, Object key) {
        if (map == null) {
            return -1;
        }

        Object val = map.get(key);
        if (val == null) {
            return -1;
        }

        try {
            return Integer.parseInt(val.toString().trim());
        } catch (NumberFormatException ex) {
            log.error("Cannot parse a port value from " + val + "; ignoring", ex);
            return -1;
        }
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
