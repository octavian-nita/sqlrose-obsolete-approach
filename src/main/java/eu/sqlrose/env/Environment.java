package eu.sqlrose.env;

import eu.sqlrose.core.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

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
