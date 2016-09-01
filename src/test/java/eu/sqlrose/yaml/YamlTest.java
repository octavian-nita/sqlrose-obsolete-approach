package eu.sqlrose.yaml;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

/**
 * @author <a href="mailto:Octavian.NITA@ext.ec.europa.eu">Octavian NITA</a>
 * @version $Id$
 */
public class YamlTest {

    protected Yaml yaml;

    @Before
    public void setUp() { yaml = new Yaml(); }

    @After
    public void tearDown() { yaml = null; }

    @Test
    public void basicLoading() {
        String docs = doc("# YAML", "- The Dagger 'Narthanc'", "- The Dagger 'Nimthanc'", "- The Dagger 'Dethanc'");
        System.out.println(joln("Loading YAML documents from", docs, NL));

        int count = 1;
        for (Object doc : yaml.loadAll(docs)) {
            System.out.println("DOC #" + count++ + ": " + doc.getClass());
        }
    }

    protected static final String NL = System.getProperty("line.separator", "\n");

    protected String doc(String... lines) { return joln(ArrayUtils.add(lines, 0, "---")); }

    protected String joln(String... args) { return join(NL, args); }

    protected String join(String sep, String... args) { return StringUtils.join(args, sep); }
}
