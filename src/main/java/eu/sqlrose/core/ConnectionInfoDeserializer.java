package eu.sqlrose.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 13, 2016
 */
class ConnectionInfoDeserializer extends JsonDeserializer<ConnectionInfo> {

    @Override
    public ConnectionInfo deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode conn = parser.getCodec().readTree(parser);

        String name = text(conn, "name");
        if (name == null) {
            log.warn("no connection name has been specified; ignoring current connection definition...");
            return null;
        }

        String driver = text(conn, "driver");
        if (driver == null) {
            log.warn("no JDBC driver has been specified and currently, only driver-based connections are supported; " +
                     "ignoring current connection definition...");
            return null;
        }

        String url = text(conn, "url");
        if (url != null) {
            return new JdbcConnectionInfo.Builder().name(name).description(text(conn, "description"))
                                                   .username(text(conn, "username")).password(text(conn, "password"))
                                                   .driverClass(driver).url(url).properties(prop(conn, "props"))
                                                   .build();
        }

        String dbms = text(conn, "dbms");
        String dbName = text(conn, "dbname");
        if (dbms == null || dbName == null) {
            log.warn("no JDBC URL or dbms and DB name have been specified; ignoring current connection definition...");
            return null;
        }

        return new JdbcConnectionInfo.Builder().name(name).description(text(conn, "description"))
                                               .username(text(conn, "username")).password(text(conn, "password"))
                                               .dbms(dbms).host(text(conn, "host")).port(port(conn, "port"))
                                               .dbName(dbName).properties(prop(conn, "props")).build();
    }

    private final Logger log = getLogger(ConnectionInfoDeserializer.class);

    private static int port(JsonNode root, String path) {
        JsonNode node = node(root, path);
        return null == node ? 0 : node.asInt(0);
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

    private static String text(JsonNode root, String path) {
        JsonNode node = node(root, path);
        if (node == null) {
            return null;
        }

        String text = node.asText();
        return isBlank(text) ? null : text.trim();
    }

    private static JsonNode node(JsonNode root, String path) {
        if (root == null || isBlank(path)) {
            return null;
        }
        JsonNode node = root.path(path);
        return node.isNull() || node.isMissingNode() ? null : node;
    }
}
