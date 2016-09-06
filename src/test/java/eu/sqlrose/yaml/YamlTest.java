package eu.sqlrose.yaml;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 31, 2016
 */
public class YamlTest {

    protected Yaml yaml;

    @Before
    public void setUp() { yaml = new Yaml(); }

    @After
    public void tearDown() { yaml = null; }

    @Test
    public void basicLoading() {
        String val = "The Dagger 'Nimthanc'";
        String src = doc("# YAML", "- The Dagger 'Narthanc'", "-   " + val + " ", "- The Dagger 'Dethanc'");

        Object doc = yaml.loadAll(src);
        assertTrue("Yaml#loadAll() expected to return a List for source " + src, doc instanceof List);

        List lov = (List) doc;
        assertEquals(3, lov.size());

        assertEquals(val, lov.get(1));
    }

    @Test
    public void advancedLoading() throws IOException {

        try (Reader src = new FileReader("yaml-test.yaml")) {
            Map<?, ?> empty = null;
            Map<?, ?> proxy = null;

            for (Object doc : yaml.loadAll(src)) {
                if (doc instanceof Map && ((Map) doc).get("empty") != null) {
                    empty = (Map) ((Map) doc).get("empty");
                }
                if (doc instanceof Map && ((Map) doc).get("proxy") != null) {
                    proxy = (Map) ((Map) doc).get("proxy");
                }
            }

            if (empty != null) {
                assertEquals("value1", empty.get("name1"));
                assertEquals("", empty.get("name2"));
                assertNull(empty.get("name3"));
                assertNull(empty.get("name4"));
            } else {
                throw new RuntimeException("No test value ('proxy') defined in test fixture");
            }

            if (proxy != null) {
                assertSame(proxy.get("http"), proxy.get("https"));
            } else {
                throw new RuntimeException("No test value ('proxy') defined in test fixture");
            }
        }
    }

    protected static final String NL = System.getProperty("line.separator", "\n");

    protected String doc(String... lines) { return joln(ArrayUtils.add(lines, 0, "---")); }

    protected String joln(String... args) { return join(NL, args); }

    protected String join(String sep, String... args) { return StringUtils.join(args, sep); }
}
