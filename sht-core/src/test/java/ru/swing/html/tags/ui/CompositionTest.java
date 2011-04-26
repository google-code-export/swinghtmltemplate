package ru.swing.html.tags.ui;

import junit.framework.TestCase;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;
import ru.swing.html.tags.Tag;

import java.io.InputStream;

/**
 * Tests &lt;compositon> tag
 */
public class CompositionTest extends TestCase {

    public void testAttribute() throws Exception {
        Composition composition = new Composition();
        String value = "foo";
        composition.setAttribute("template", value);
        assertEquals(value, composition.getTemplate());
    }

    public void testComposition() throws Exception {

        InputStream source = getClass().getResourceAsStream("CompositionSource.html");

        DomModel model = DomLoader.loadModel(source);
        assertNotNull(model);

        Tag html = model.getRootTag();
        assertEquals("html", html.getName());
        assertEquals(2, html.getChildren().size());//<head> and <body>
        assertEquals("head", html.getChildren().get(0).getName());

        Tag body = html.getChildren().get(1);
        assertEquals("body", body.getName());

        assertEquals("body", body.getAttribute("id"));
        assertEquals("border", body.getAttribute("display"));

        assertEquals(2, body.getChildren().size());

        Tag contentWrapper = body.getChildren().get(0);
        Tag controlWrapper = body.getChildren().get(1);

        assertEquals("center", contentWrapper.getAlign());
        assertEquals("bottom", controlWrapper.getAlign());

        assertEquals(1, contentWrapper.getChildren().size());
        Tag scroll = contentWrapper.getChildren().get(0);
        assertEquals("scroll", scroll.getName());
        assertEquals(1, scroll.getChildren().size());
        assertEquals("textarea", scroll.getChildren().get(0).getName());

        assertEquals(1, controlWrapper.getChildren().size());
        Tag div = controlWrapper.getChildren().get(0);
        assertEquals("div", div.getName());
        assertEquals(2, div.getChildren().size());
        assertEquals("input", div.getChildren().get(0).getName());
        assertEquals("OK", div.getChildren().get(0).getAttribute("text"));
        assertEquals("input", div.getChildren().get(1).getName());
        assertEquals("Cancel", div.getChildren().get(1).getAttribute("text"));




    }


    public void testCompositionWithInclude() throws Exception {
        InputStream source = getClass().getResourceAsStream("CompositionIncludeSource.html");

        DomModel model = DomLoader.loadModel(source);
        assertNotNull(model);

        Tag html = model.getRootTag();
        assertEquals("html", html.getName());
        assertEquals(2, html.getChildren().size());//<head> and <body>
        assertEquals("head", html.getChildren().get(0).getName());

        Tag body = html.getChildren().get(1);
        assertEquals("body", body.getName());

        assertEquals(1, body.getChildren().size());
        assertEquals("p", body.getChildren().get(0).getName());
        assertEquals("Hello world", body.getChildren().get(0).getContent());

    }
}
