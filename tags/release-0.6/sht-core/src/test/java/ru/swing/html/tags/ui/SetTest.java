package ru.swing.html.tags.ui;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;

import javax.swing.*;
import java.io.ByteArrayInputStream;

public class SetTest extends TestCase {

    public void testTagAttributes() throws Exception {
        Set set = new Set();
        set.setAttribute("var", "foo");
        set.setAttribute("value", "${foo}");

        assertEquals("foo", set.getVar());
        assertEquals("${foo}", set.getValue());
    }


    public void testModelElement() throws Exception {
        String html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:c=\"http://www.oracle.com/swing\"\n" +
                        "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <ui:set var='foo' value='${show}'/>" +
                "   <p id='p'>${foo}</p>" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        model.addModelElement("show", "fooString");
        DomConverter.toSwing(model);

        assertEquals("fooString", model.getModelElements().get("foo"));

        JLabel label = (JLabel) model.getTagById("p").getComponent();
        assertEquals("fooString", label.getText());

        //change source model element and check components have old value
        model.addModelElement("show", "fooString1");
        assertEquals("fooString", model.getModelElements().get("foo"));
        assertEquals("fooString", label.getText());
    }

    public void testBinding() throws Exception {
        String html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:c=\"http://www.oracle.com/swing\"\n" +
                        "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <ui:set var='foo' value='${show}' type='binding'/>" +
                "   <p id='p' content='el'>${foo}</p>" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        model.addModelElement("show", "fooString");
        DomConverter.toSwing(model);

        assertEquals("fooString", model.getModelElements().get("foo"));

        JLabel label = (JLabel) model.getTagById("p").getComponent();
        assertEquals("fooString", label.getText());

        //change source model element and check components have new value
        model.addModelElement("show", "fooString1");
        assertEquals("fooString1", model.getModelElements().get("foo"));
        assertEquals("fooString1", label.getText());

    }

    public void testComplexValue() throws Exception {
        String html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:c=\"http://www.oracle.com/swing\"\n" +
                        "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <ui:set var='text' value='${num > 0 ? 1 : 0} items'/>" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        model.addModelElement("num", 1);
        DomConverter.toSwing(model);

        assertEquals("1 items", model.getModelElements().get("text"));


    }
    


}
