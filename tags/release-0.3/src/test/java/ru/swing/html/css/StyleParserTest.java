package ru.swing.html.css;

import junit.framework.TestCase;

import java.util.List;
import java.util.Map;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 22.11.2010
 * Time: 10:30:20
 * </pre>
 */
public class StyleParserTest extends TestCase {


    public void testExtractStyles() {
        String style = "foo1:val1; foo2 : val2; foo3: val3;\nfoo4:\tval4";
        Map<String, String> styles = StyleParser.extractStyles(style);

        assertTrue(style.contains("foo1"));
        assertTrue(style.contains("foo2"));
        assertTrue(style.contains("foo3"));
        assertTrue(style.contains("foo4"));

        assertEquals("val1", styles.get("foo1"));
        assertEquals("val2", styles.get("foo2"));
        assertEquals("val3", styles.get("foo3"));
        assertEquals("val4", styles.get("foo4"));
    }


    public void testExtractCssBlocks() throws Exception {
        List<String> blocks = StyleParser.extractCssBlocks(".foo1 { nam1: val1} foo2 { nam2: val2 }");
        assertEquals(2, blocks.size());

        blocks = StyleParser.extractCssBlocks(".foo1 { nam1: val1}\n foo2 { nam2: val2 }");
        assertEquals(2, blocks.size());

        blocks = StyleParser.extractCssBlocks("\n" +
                "\n" +
                "        p {\n" +
                "            color: red;\n" +
                "        }\n" +
                "    ");
        assertEquals(1, blocks.size());


    }

    public void testParseCssBlock() throws Exception {
        CssBlock block = StyleParser.parseCssBlock(".foo1 { nam1: val1}");
        assertNotNull(block);

        assertEquals(1, block.getStyles().size());

        block = StyleParser.parseCssBlock(".foo1, p, #test { nam1: val1}");
        assertNotNull(block);

        assertEquals(1, block.getStyles().size());

    }


}
