package eu.sqlrose.core;

import eu.sqlrose.annotations.Serializable;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 08, 2016
 */
@Serializable
public class JdbcConnectionInfo extends ConnectionInfo {

    private static final long serialVersionUID = 1020160708L;

    //protected final String driverClass;
    //
    //protected final String url;
    //
    //protected final Properties properties;
    //
    ///** The dbms protocol could include an optional subsubprotocol. */
    //protected final String dbms;
    //
    //protected final String host;
    //
    //protected final String port;
    //
    //protected final String dbName;

    public JdbcConnectionInfo(String name, String description, String username, String password) {
        super(name, description, username, password);
    }

    public JdbcConnectionInfo(String name, String description) {
        super(name, description);
    }

    public JdbcConnectionInfo(String name) {
        super(name);
    }

    public static void main(String[] args) {

        System.out.println("Done!");
    }
}
