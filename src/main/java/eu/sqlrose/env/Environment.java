package eu.sqlrose.env;

import eu.sqlrose.core.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.*;

/**
 * The {@link #load(String, String...) loading} {@link #load(URL, URL...) capabilities} provided by default never throw
 * any exceptions; in case of invalid input or any other loading error they log and do not modify the currently loading
 * environment (<code>this</code>) with the erroneous entry. Moreover, they try to load only structures they recognize,
 * silently ignoring the rest of the configuration content.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 11, 2016
 */
public class Environment implements Serializable {

    protected final transient Logger log = LoggerFactory.getLogger(Environment.class);

    //
    // The system-wide authenticator set through the current environment
    //

    private transient Authenticator defaultAuthenticator;

    public Authenticator getDefaultAuthenticator() { return defaultAuthenticator; }

    public void setDefaultAuthenticator(Authenticator defaultAuthenticator) {
        Authenticator.setDefault(this.defaultAuthenticator = defaultAuthenticator);
    }

    public void setDefaultAuthenticator(String username, String password) {
        setDefaultAuthenticator(new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password == null ? new char[]{} : password.toCharArray());
            }
        });
    }

    //
    // Additional system properties set through the current environment
    //

    private final Properties systemProperties = new Properties();

    /**
     * @return only the system properties {@link #setSystemProperties(Properties) set} through <code>this</code>
     * {@link Environment}
     */
    public Properties getSystemProperties() {
        Properties prop = new Properties();
        prop.putAll(this.systemProperties);
        return prop; // defensive copy
    }

    public void setSystemProperties(Properties properties) {
        this.systemProperties.clear();
        if (properties != null) {
            this.systemProperties.putAll(properties);
            System.setProperties(properties);
        }
    }

    public void setSystemProperties(Map<?, ?> properties) {
        this.systemProperties.clear();
        if (properties != null) {
            this.systemProperties.putAll(properties);
            System.setProperties(this.systemProperties);
        }
    }

    //
    // As container of DataSources
    //

    private final Map<String, DataSource> dataSources = new LinkedHashMap<>();

    public void setDataSources(Collection<? extends DataSource> dataSources) {
        this.dataSources.clear();
        if (dataSources != null) {
            dataSources.stream().filter(ds -> ds != null).forEach(ds -> this.dataSources.put(ds.getName(), ds));
        }
    }

    public Collection<DataSource> getDataSources() { return Collections.unmodifiableCollection(dataSources.values()); }

    public DataSource getDataSource(String name) { return dataSources.get(name); }

    public void add(DataSource dataSource) {
        if (dataSource == null) {
            log.error("Cannot add a null data source to the environment; ignoring...");
        } else {
            dataSources.put(dataSource.getName(), dataSource);
        }
    }

    public void remove(DataSource dataSource) {
        if (dataSource == null) {
            log.error("Cannot remove a null data source from the environment; ignoring...");
        } else {
            dataSources.remove(dataSource.getName());
        }
    }

    //
    // Convenient environment loading
    //

    public Environment load(String content, String... otherContent) {
        return new EnvironmentLoader().load(this, content, otherContent);
    }

    public Environment load(URL url, URL... otherURLs) { return new EnvironmentLoader().load(this, url, otherURLs); }
}
