package ru.swing.html.xhtmlrenderer;

import junit.framework.TestCase;
import org.jdom.Element;
import ru.swing.html.css.StyleParser;
import ru.swing.html.tags.Div;
import ru.swing.html.tags.P;
import ru.swing.html.tags.Tag;

import java.util.Map;

public class UtilsTest extends TestCase {

    public void testReplaceCssAttribute() throws Exception {

        //check replacing
        String css = "text-align: center; display: border;";
        String converted = Utils.replaceCssAttribute(css, "display", "block");
        Map<String, String> styles = StyleParser.extractStyles(converted);
        assertEquals("block", styles.get("display"));
        assertEquals("center", styles.get("text-align"));

        //check adding
        css = "text-align: center;";
        converted = Utils.replaceCssAttribute(css, "display", "block");
        styles = StyleParser.extractStyles(converted);
        assertEquals("block", styles.get("display"));
        assertEquals("center", styles.get("text-align"));


    }


    public void testTagToElement() throws Exception {

        Div div = new Div();
        div.setName("div");
        div.setAttribute("style", "display:border;");
        div.setAttribute("renderer", "swing");

        Element e = Utils.tagToElement(div);
        assertEquals("div", e.getName());
        assertEquals("swing", e.getAttribute("renderer").getValue());
        String style = e.getAttribute("style").getValue();
        Map<String, String> styles = StyleParser.extractStyles(style);
        assertEquals("block", styles.get("display"));




        Tag content = new Tag();
        content.setName("br");

        P p = new P();
        p.setName("p");
        p.setAttribute("style", "display:inline;");
        p.addContentChild("text1");
        p.addContentChild(content);
        p.addContentChild("text2");


        e = Utils.tagToElement(p);

        assertEquals("p", e.getName());
        style = e.getAttribute("style").getValue();
        styles = StyleParser.extractStyles(style);
        assertEquals("inline", styles.get("display"));

        assertEquals(0, e.getContentSize());

    }

    public void testConvert() throws Exception {
        Tag content = new Tag();
        content.setName("br");

        P p = new P();
        p.setName("p");
        p.addContentChild("text1");
        p.addContentChild(content);
        p.addContentChild("text2");

        Element e = new Element("p");

        Utils.convert(e, p);


        assertEquals(3, e.getContentSize());
        assertEquals("text1", e.getContent(0).getValue());
        assertEquals("br", ((Element)e.getContent(1)).getName());
        assertEquals("text2", e.getContent(2).getValue());

    }
}
