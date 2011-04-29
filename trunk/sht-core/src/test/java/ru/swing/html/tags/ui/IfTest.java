package ru.swing.html.tags.ui;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;

import javax.swing.*;
import java.io.ByteArrayInputStream;

public class IfTest extends TestCase {

    public void testIfTrue() throws Exception {
        String html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:c=\"http://www.oracle.com/swing\"\n" +
                        "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <ui:if test='${show}'>" +
                "      <label id='lbl'>Label</label>" +
                "   </ui:if>" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        model.addModelElement("show", true);
        JComponent root = DomConverter.toSwing(model);
        assertNotNull(root);

        assertEquals(1, root.getComponentCount());
    }

    public void testIfFalse() throws Exception {
        String html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:c=\"http://www.oracle.com/swing\"\n" +
                        "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <ui:if test='${show}'>" +
                "      <label id='lbl'>Label</label>" +
                "   </ui:if>" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        model.addModelElement("show", false);
        JComponent root = DomConverter.toSwing(model);
        assertNotNull(root);

        assertEquals(0, root.getComponentCount());
    }

    public void testIfFalseCompoundEl() throws Exception {
        String html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:c=\"http://www.oracle.com/swing\"\n" +
                        "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <ui:if test='${show or display}'>" +
                "      <label id='lbl'>Label</label>" +
                "   </ui:if>" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        model.addModelElement("show", false);
        model.addModelElement("display", false);
        JComponent root = DomConverter.toSwing(model);
        assertNotNull(root);

        assertEquals(0, root.getComponentCount());

        model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        model.addModelElement("show", false);
        model.addModelElement("display", true);
        root = DomConverter.toSwing(model);
        assertNotNull(root);

        assertEquals(1, root.getComponentCount());
    }
}
