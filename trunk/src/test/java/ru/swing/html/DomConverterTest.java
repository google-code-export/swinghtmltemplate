package ru.swing.html;

import junit.framework.TestCase;
import ru.swing.html.css.CssBlock;
import ru.swing.html.tags.Tag;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 22.11.2010
 * Time: 13:21:41
 * </pre>
 */
public class DomConverterTest extends TestCase {

    public void testParseHead() throws Exception {

        DomModel model = new DomModel();
        Tag head = new Tag();
        Tag style = new Tag();
        style.setName("style");
        style.setContent(
                ".foo { " +
                        "   name1: val1" +
                        "}" +
                        "" +
                        "p.red {" +
                        "  color: red;" +
                        "  font-weight: bold;" +
                        "}");
        head.addChild(style);
        DomConverter.parseHead(model, head);
        assertEquals(2, model.getGlobalStyles().size());

        CssBlock block = model.getGlobalStyles().get(0);
        Tag tag = new Tag();
        tag.setAttribute("class", "foo");
        assertTrue(block.matches(tag));
        assertEquals(1, block.getStyles().size());

    }
}
