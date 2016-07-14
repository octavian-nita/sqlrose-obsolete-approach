package eu.sqlrose.core.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:Octavian.NITA@ext.ec.europa.eu">Octavian NITA</a>
 * @version $Id$
 */
public class JacksonTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() { mapper = new YAMLMapper().setPropertyNamingStrategy(SNAKE_CASE); }

    @After
    public void tearDown() { mapper = null; }

    @Test
    public void pathReturnsOnlyMissing() throws IOException {
        JsonNode root = mapper.readTree(getClass().getClassLoader().getResource("jackson-test.yml")), node;

        node = root.path("name1");
        assertNotNull(node);
        assertFalse(node.isMissingNode());
        assertEquals("value1", node.asText());

        node = root.path("name2");
        assertNotNull(node);
        assertFalse(node.isMissingNode());
        assertEquals("", node.asText());

        node = root.path("name3");
        assertNotNull(node);
        assertTrue(node.isNull());

        node = root.path("name4");
        assertNotNull(node);
        assertTrue(node.isMissingNode());
    }
}
