package eu.sqlrose.yaml;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test case illustrating basic {@link Yaml} usage in SQLrose.
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Aug 31, 2016
 */
public class YamlTest {

    protected Yaml yamlUnderTest;

    protected String yamlScalar;

    protected String yamlListWithScalarInSecondPositionDoc;

    @Before
    public void setUp() {
        yamlUnderTest = new Yaml();
        yamlScalar = "The Dagger 'Nimthanc'";
        yamlListWithScalarInSecondPositionDoc =
            doc("# YAML", "- The Dagger 'Narthanc'", "-   " + yamlScalar + " ", "- The Dagger 'Dethanc'");
    }

    @After
    public void tearDown() {
        yamlUnderTest = null;
        yamlScalar = null;
        yamlListWithScalarInSecondPositionDoc = null;
    }

    @Test
    public void GIVEN_EmptyDocument_WHEN_LoadAllCalled_THEN_ReturnedIterableHasNoRoots() {
        Iterable<?> roots = yamlUnderTest.loadAll("  ");
        assertNotNull(roots);

        Iterator<?> it = roots.iterator();
        assertFalse(it.hasNext());
    }

    @Test
    public void GIVEN_SingleDocument_WHEN_LoadAllCalled_THEN_ReturnedIterableHasExactlyOneRoot() {
        Iterable<?> roots = yamlUnderTest.loadAll(yamlListWithScalarInSecondPositionDoc);
        assertNotNull(roots);

        Iterator<?> it = roots.iterator();
        assertTrue(it.hasNext());

        Object root = it.next();
        assertNotNull(root);

        assertFalse(it.hasNext());
    }

    @Test
    public void GIVEN_ListWithScalar_WHEN_LoadAllCalled_THEN_ReturnedValueIsInstanceOfListContainingScalar() {
        Object root = yamlUnderTest.loadAll(yamlListWithScalarInSecondPositionDoc).iterator().next();
        assertTrue(root instanceof List);
        assertTrue(((List) root).contains(yamlScalar));
    }

    /**
     * Simple test template.
     */
    @Test
    public void GIVEN_SomeContent_WHEN_LoadAllCalled_THEN_ReturnStructureAsExpected() {

        String someContent = joln(//@fmt:off
            "- k.0.0: v.0.0",
            "  k.0.1: v.0.1",
            "- k.1.0: v.1.0",
            "  k.1.1: v.1.1");//@fmt:on

        Object root = yamlUnderTest.loadAll(someContent).iterator().next();
    }

    @Test
    public void GIVEN_MultipleComplexDocuments_WHEN_LoadAllCalled_THEN_ReturnStructureAsExpected() throws IOException {

        String complexYamlContent = joln(//@fmt:off
            "---",
            "empty:",
            "  name1:   value1",
            "  name2: \"\"",
            "  name3:",
            "",
            "---",
            "proxy:",
            "  http:   &httpAlias",
            "    host: 192.168.1.1",
            "    port: 8023",
            "    user: user",
            "    pass: pass",
            "  https:  *httpAlias");//@fmt:on

        Iterator<?> it = yamlUnderTest.loadAll(complexYamlContent).iterator();

        Map<?, ?> empty = (Map) ((Map) it.next()).get("empty");
        assertEquals("value1", empty.get("name1"));
        assertEquals("", empty.get("name2"));
        assertNull(empty.get("name3"));
        assertNull(empty.get("name4"));

        Map<?, ?> proxy = (Map) ((Map) it.next()).get("proxy");
        assertSame(proxy.get("http"), proxy.get("https"));
    }

    protected static final String NL = System.getProperty("line.separator", "\n");

    protected String doc(String... lines) { return joln(ArrayUtils.insert(0, lines, "---")); }

    protected String joln(String... args) { return join(NL, args); }

    protected String join(String sep, String... args) { return StringUtils.join(args, sep); }
}
