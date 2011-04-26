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

    public void testMatches2() throws Exception {

        String html = "<html>" +
                "<body>" +
                "  <a rel='en-1'/>" +
                "  <a rel='en-2'/>" +
                "  <a rel='en3'/>" +
                "  <a rel='ru1'/>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));

        Tag[] res = model.query("a[rel|=en]");
        assertEquals(2, res.length);

    }

    public void testMatches3() throws Exception {

        String html = "<html>" +
                "<body>" +
                "  <a class='red top'/>" +
                "  <a class='red'/>" +
                "  <a class='top'/>" +
                "  <a class='red top foo'/>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));

        assertEquals(3, model.query(".red").length);
        assertEquals(3, model.query(".top").length);
        assertEquals(2, model.query(".red.top").length);
        assertEquals(1, model.query(".red.top.foo").length);
        assertEquals(2, model.query("*[class~='red top']").length);

    }

    public void testMatches4() throws Exception {

        String html = "<html>" +
                "<body>" +
                "  <a id='a1'/>" +
                "  <a id='a2'/>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));

        assertEquals(1, model.query("#a1").length);
        assertEquals(1, model.query("#a2").length);

    }

    public void testMatches5() throws Exception {

        String html = "<html>" +
                "<body>" +
                "  <span/>"+
                "  <div class='foo'>" +
                "     <div class='foo'>"+
                "        <p/>"+
                "     </div>"+
                "  </div>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));

        assertEquals(1, model.query("span+div.foo p").length);

    }
}
