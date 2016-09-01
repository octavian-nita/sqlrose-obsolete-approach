package eu.sqlrose.env;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 13, 2016
 */
class DataSourceDeserializer /*extends JsonDeserializer<DataSource>*/ {

//    static class InvalidDataSource extends JsonProcessingException {
//
//        InvalidDataSource(String message) { super(message); }
//
//        InvalidDataSource(String message, Throwable cause) { super(message, cause); }
//    }
//
//    @Override
//    public DataSource deserialize(JsonParser parser, DeserializationContext context) throws InvalidDataSource {
//        JsonNode ds;
//        try {
//            ds = parser.getCodec().readTree(parser);
//        } catch (IOException ioe) {
//            throw new InvalidDataSource("cannot parse the data source definition", ioe);
//        }
//
//        String name = text(ds, "name");
//        if (name == null) {
//            throw new InvalidDataSource("no data source name has been specified");
//        }
//
//        String driver = text(ds, "driver");
//        if (driver == null) {
//            throw new InvalidDataSource("no JDBC driver has been specified for data source '" + name +
//                                        "' and currently only driver-based data sources are supported");
//        }
//
//        String url = text(ds, "url");
//        if (url != null) {
//            return new DriverBasedDataSource.Builder().name(name).description(text(ds, "description"))
//                                                      .username(text(ds, "username")).password(text(ds, "password"))
//                                                      .driverClass(driver).url(url).properties(prop(ds, "props"))
//                                                      .build();
//        }
//
//        String dbms = text(ds, "dbms");
//        String dbName = text(ds, "dbname");
//        if (dbms == null || dbName == null) {
//            throw new InvalidDataSource("no JDBC URL or dbms and DB name have been specified...");
//        }
//
//        return new DriverBasedDataSource.Builder().name(name).description(text(ds, "description"))
//                                                  .username(text(ds, "username")).password(text(ds, "password"))
//                                                  .driverClass(driver).dbms(dbms).host(text(ds, "host"))
//                                                  .port(port(ds, "port")).dbName(dbName).properties(prop(ds, "props"))
//                                                  .build();
//    }
//
//    private static int port(JsonNode root, String path) {
//        JsonNode node = node(root, path);
//        return node == null ? 0 : node.asInt(0);
//    }
//
//    private static String text(JsonNode root, String path) {
//        JsonNode node = node(root, path);
//        if (node == null) {
//            return null;
//        }
//
//        String text = node.asText();
//        return isBlank(text) ? null : text.trim();
//    }
//
//    private static Properties prop(JsonNode root, String path) {
//        JsonNode node = node(root, path);
//        if (node == null) {
//            return null;
//        }
//
//        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
//        if (fields.hasNext()) {
//            Properties props = new Properties();
//            while (fields.hasNext()) {
//                Map.Entry<String, JsonNode> field = fields.next();
//                props.put(field.getKey(), field.getValue().asText());
//            }
//            return props;
//        }
//
//        return null;
//    }
//
//    private static JsonNode node(JsonNode root, String path) {
//        if (root == null || isBlank(path)) {
//            return null;
//        }
//        JsonNode node = root.path(path);
//        return node.isNull() || node.isMissingNode() ? null : node;
//    }
}
