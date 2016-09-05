package eu.sqlrose.core;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Sep 05, 2016
 */
public enum E implements ErrorCode {

    DS_CANNOT_CONNECT,
    DS_CANNOT_DISCONNECT;

    @Override
    public String getCode() { return "E_" + name(); }
}
