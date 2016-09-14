package eu.sqlrose.env;

import eu.sqlrose.core.JdbcDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Sep 07, 2016
 */
class EnvironmentLoader {

    Environment load(String content, String... otherContent) { return load(null, content, otherContent); }

    Environment load(Environment target, String content, String... otherContent) {
        if (target == null) {
            target = new Environment();
        }

        final Yaml yaml = new Yaml();
        load(target, content, yaml);
        if (otherContent != null) {
            for (String other : otherContent) {
                load(target, other, yaml);
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
        load(target, url, yaml);
        if (otherURLs != null) {
            for (URL other : otherURLs) {
                load(target, other, yaml);
            }
        }

        return target;
    }

    private final Logger log = LoggerFactory.getLogger(Environment.class);

    private void load(Environment target, String content, Yaml yaml) {
        if (validate(target, content, yaml)) {
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

    private void load(Environment target, URL url, Yaml yaml) {
        if (validate(target, url, yaml)) {
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

    private boolean validate(Environment target, Object source, Yaml yaml) {
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
        if (root != null && root instanceof Map) {

            // Data sources (allow both a collection as well as a single element, even at the same time):
            loadDataSources(target, ((Map) root).get("data_sources"));
            loadDataSources(target, ((Map) root).get("data_source"));

            // Eventual (additional) Java system properties:
            loadSystemProperties(target, ((Map) root).get("system_properties"));

            // Eventual authenticator data:
            loadDefaultAuthenticator(target, ((Map) root).get("default_authenticator"));

        }
    }

    private void loadDataSources(Environment target, Object dsRoot) {
        if (dsRoot == null) {
            return;
        }

        if (dsRoot instanceof Map) {
            Map ds = (Map) dsRoot;

            String name = text(ds, "name");
            String driver = text(ds, "driver");
            if (driver == null) {

                log.error("No JDBC driver has been specified for data source '" + name +
                          "' and currently only driver-based data sources are supported; ignoring...");

            } else {

                String url = text(ds, "url");
                try {
                    if (url != null) {

                        target.add(new JdbcDataSource.Builder().name(name).description(text(ds, "description"))
                                                               .username(text(ds, "username"))
                                                               .password(text(ds, "password")).driverClass(driver)
                                                               .url(url).properties(prop(ds, "props")).build());

                    } else {

                        String port = text(ds, "port");
                        target.add(new JdbcDataSource.Builder().name(name).description(text(ds, "description"))
                                                               .username(text(ds, "username"))
                                                               .password(text(ds, "password")).driverClass(driver)
                                                               .dbms(text(ds, "dbms")).host(text(ds, "host"))
                                                               .port(port == null ? null : new Integer(port))
                                                               .dbName(text(ds, "dbname")).properties(prop(ds, "props"))
                                                               .build());
                    }
                } catch (Throwable throwable) {
                    log.error("Cannot load data source " + name + " (see details below); ignoring...", throwable);
                }

            }

            return;
        }

        if (dsRoot instanceof Collection) {
            for (Object ds : (Collection) dsRoot) {
                loadDataSources(target, ds);
            }
            return;
        }

        log.error(
            "Data source specifications have to be either a mapping of 'key: value' pairs (i.e. one data source) or a" +
            " collection of such mappings; ignoring...");
    }

    private void loadSystemProperties(Environment target, Object spRoot) {
        if (spRoot == null) {
            return;
        }

        if (!(spRoot instanceof Map)) {
            log.error("Additional system properties have to be a mapping of 'key: value' pairs; ignoring...");
            return;
        }

        target.setSystemProperties((Map) spRoot);
    }

    private void loadDefaultAuthenticator(Environment target, Object daRoot) {
        if (daRoot == null) {
            return;
        }

        if (!(daRoot instanceof Map)) {
            log.error("Default authenticator data has to be a mapping of 'key: value' pairs; ignoring...");
            return;
        }

        target.setDefaultAuthenticator(text((Map) daRoot, "username"), text((Map) daRoot, "password"));
    }

    // 'Helper' methods always return <code>null</code> to indicate the absence of a value!
    // (even for empty / blank string values since we are interested only by specified values)

    private static String text(Map<?, ?> map, Object key) {
        Object val = map == null ? null : map.get(key);
        if (val == null) {
            return null;
        }

        String str = val.toString().trim();
        return str.length() == 0 ? null : str;
    }

    private static Properties prop(Map<?, ?> map, Object key) {
        Object val = map == null ? null : map.get(key);
        if (val == null || !(val instanceof Map)) {
            return null;
        }

        Properties prop = new Properties();
        prop.putAll((Map) val);
        return prop;
    }
}
