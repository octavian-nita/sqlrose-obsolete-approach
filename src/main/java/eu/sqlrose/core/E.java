package eu.sqlrose.core;

/**
 * @author <a href="mailto:Octavian.NITA@ext.ec.europa.eu">Octavian NITA</a>
 * @version $Id$
 */
public enum E implements ErrorCode {

    DS_CANNOT_CONNECT,
    DS_CANNOT_DISCONNECT;

    @Override
    public String getCode() { return "E_" + name(); }
}
