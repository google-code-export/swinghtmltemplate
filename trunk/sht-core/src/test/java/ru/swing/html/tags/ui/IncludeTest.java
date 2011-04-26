package ru.swing.html.tags.ui;

import junit.framework.TestCase;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;
import ru.swing.html.tags.Tag;

import java.io.InputStream;

public class IncludeTest extends TestCase {

    public void testAttributes() throws Exception {
        Include include = new Include();
        String value = "foo";
        include.setAttribute("src", value);
        assertEquals("'src' attribute must be treated as source", value, include.getSource());
    }

    public void testInclude() throws Exception {
        InputStream source = getClass().getResourceAsStream("IncludeSource.html");

        DomModel model = DomLoader.loadModel(source);
        assertNotNull(model);

        Tag div = model.getTagById("bottom");
        assertEquals(2, div.getChildren().size());

        Tag input1 = div.getChildren().get(0);
        Tag input2 = div.getChildren().get(1);
        assertEquals("OK", input1.getAttribute("text"));
        assertEquals("Cancel", input2.getAttribute("text"));
    }
}
