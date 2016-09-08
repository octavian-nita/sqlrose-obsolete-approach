package eu.sqlrose.env;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.Serializable;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Sep 8, 2016
 */
public class ProxyConfig implements Serializable {

    private final String host;

    private final int port;

    private final List<String> nonProxyHosts = new ArrayList<>();

    private transient String username;

    private transient char[] password;

    public enum Protocol {
        HTTP,
        HTTPS,
        FTP,
        SOCKS;

        public String prefix() { return name().toLowerCase() + "."; }
    }

    public ProxyConfig(String host, int port, String username, char[] password, String... nonProxyHosts) {

        Validate.notBlank(host, "The proxy host cannot be null, empty or whitespace-only");
        Validate.inclusiveBetween(0, 65535, port, "The proxy port has to be between 0 and 65535");

        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password == null ? null : Arrays.copyOf(password, password.length);

        if (nonProxyHosts != null) {
            for (String nonProxyHost : nonProxyHosts) {
                if (StringUtils.isNotBlank(nonProxyHost)) {
                    this.nonProxyHosts.add(nonProxyHost.trim());
                }
            }
        }
    }

    public ProxyConfig(String host, int port, String username, String password, String... nonProxyHosts) {
        this(host, port, username, password == null ? null : password.toCharArray(), nonProxyHosts);
    }

    public ProxyConfig(String host, int port, String... nonProxyHosts) {
        this(host, port, null, (char[]) null, nonProxyHosts);
    }

    public void install(Protocol... forProtocols) {
        if (forProtocols == null || forProtocols.length == 0) {
            forProtocols = new Protocol[]{Protocol.HTTP, Protocol.HTTPS};
        }

        final String port = String.valueOf(this.port);

        for (Protocol protocol : forProtocols) {
            if (protocol != null) {
                String prefix = protocol.prefix();

                System.setProperty(prefix + "proxyHost", host);
                System.setProperty(prefix + "proxyPort", port);

                if (username != null && password != null) {
                    System.setProperty(prefix + "proxyUser", username);
                    System.setProperty(prefix + "proxyPassword", String.valueOf(password));
                }

                if (!nonProxyHosts.isEmpty()) {
                    System.setProperty((protocol == Protocol.HTTPS ? "http" : prefix) + "nonProxyHosts",
                                       StringUtils.join(nonProxyHosts, '|'));
                }
            }
        }

        if (username != null && password != null) {
            Authenticator.setDefault(new Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
        }
    }
}
