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

    public void testNamespaceSelector() throws Exception {
        Selector selector = new Selector("j:tree");
        assertNotNull(selector);
    }

    public void testMatches() throws Exception {

        String html = "<html>" +
                "<body>" +
                "  <div class='red'><p/></div>" +
                "  <div class='red'><p/><span/></div>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));

        assertEquals(1, model.query("html div.red p+*").size());

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

        assertEquals(2, model.query("a[rel|=en]").size());

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

        assertEquals(3, model.query(".red").size());
        assertEquals(3, model.query(".top").size());
        assertEquals(2, model.query(".red.top").size());
        assertEquals(1, model.query(".red.top.foo").size());
        assertEquals(2, model.query("*[class~='red top']").size());

    }

    public void testMatches4() throws Exception {

        String html = "<html>" +
                "<body>" +
                "  <a id='a1'/>" +
                "  <a id='a2'/>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));

        assertEquals(1, model.query("#a1").size());
        assertEquals(1, model.query("#a2").size());

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

        assertEquals(1, model.query("span+div.foo p").size());

    }
}
