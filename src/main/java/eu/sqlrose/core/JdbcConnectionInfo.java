package eu.sqlrose.core;

import java.util.Properties;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 08, 2016
 */
public class JdbcConnectionInfo extends ConnectionInfo {

    private static final long serialVersionUID = 1020160708L;

    protected final String driverClass;

    protected final String url;

    protected final Properties properties;

    /** The dbms protocol could include an optional subsubprotocol. */
    protected final String dbms;

    protected final String host;

    protected final String port;

    protected final String dbName;

}
