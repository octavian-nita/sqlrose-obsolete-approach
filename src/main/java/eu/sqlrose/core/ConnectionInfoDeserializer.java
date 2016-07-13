package eu.sqlrose.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 13, 2016
 */
class ConnectionInfoDeserializer extends JsonDeserializer<ConnectionInfo> {

    @Override
    public ConnectionInfo deserialize(JsonParser p, DeserializationContext context)
        throws IOException, JsonProcessingException {
        return null;
    }
}
