package eu.sqlrose.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 13, 2016
 */
class ConnectionInfoDeserializer extends JsonDeserializer<ConnectionInfo> {

    static class InvalidConnectionInfo extends JsonProcessingException {

        InvalidConnectionInfo(String message) { super(message); }

        InvalidConnectionInfo(String message, Throwable cause) { super(message, cause); }
    }

    @Override
    public ConnectionInfo deserialize(JsonParser parser, DeserializationContext context) throws InvalidConnectionInfo {
        JsonNode conn;
        try {
            conn = parser.getCodec().readTree(parser);
        } catch (IOException ioe) {
            throw new InvalidConnectionInfo("cannot parse the connection definition", ioe);
        }

        String name = text(conn, "name");
        if (name == null) {
            throw new InvalidConnectionInfo("no connection name has been specified");
        }

        String driver = text(conn, "driver");
        if (driver == null) {
            throw new InvalidConnectionInfo("no JDBC driver has been specified for connection '" + name +
                                            "' and currently only driver-based connections are supported");
        }

        String url = text(conn, "url");
        if (url != null) {
            return new DriverBasedConnectionInfo.Builder().name(name).description(text(conn, "description"))
                                                          .username(text(conn, "username"))
                                                          .password(text(conn, "password")).driverClass(driver).url(url)
                                                          .properties(prop(conn, "props")).build();
        }

        String dbms = text(conn, "dbms");
        String dbName = text(conn, "dbname");
        if (dbms == null || dbName == null) {
            throw new InvalidConnectionInfo("no JDBC URL or dbms and DB name have been specified...");
        }

        return new DriverBasedConnectionInfo.Builder().name(name).description(text(conn, "description"))
                                                      .username(text(conn, "username")).password(text(conn, "password"))
                                                      .driverClass(driver).dbms(dbms).host(text(conn, "host"))
                                                      .port(port(conn, "port")).dbName(dbName)
                                                      .properties(prop(conn, "props")).build();
    }

    private static int port(JsonNode root, String path) {
        JsonNode node = node(root, path);
        return node == null ? 0 : node.asInt(0);
    }

    private static String text(JsonNode root, String path) {
        JsonNode node = node(root, path);
        if (node == null) {
            return null;
        }

        String text = node.asText();
        return isBlank(text) ? null : text.trim();
    }

    private static Properties prop(JsonNode root, String path) {
        JsonNode node = node(root, path);
        if (node == null) {
            return null;
        }

        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        if (fields.hasNext()) {
            Properties props = new Properties();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                props.put(field.getKey(), field.getValue().asText());
            }
            return props;
        }

        return null;
    }

    private static JsonNode node(JsonNode root, String path) {
        if (root == null || isBlank(path)) {
            return null;
        }
        JsonNode node = root.path(path);
        return node.isNull() || node.isMissingNode() ? null : node;
    }
}
