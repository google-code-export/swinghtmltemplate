package ru.swing.html.css;

import junit.framework.TestCase;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;
import ru.swing.html.tags.Tag;

import java.io.ByteArrayInputStream;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 22.11.2010
 * Time: 13:03:26
 * </pre>
 */
public class SelectorTest extends TestCase {

    public void testMatches() throws Exception {

        String html = "<html>" +
                "<body>" +
                "  <div class='red'><p/></div>" +
                "  <div class='red'><p/><span/></div>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));

        Tag[] res = model.query("html div.red p+*");
        assertEquals(1, res.length);

    }
}
