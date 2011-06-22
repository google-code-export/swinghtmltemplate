package ru.swing.html.tags;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.util.Arrays;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 23.11.2010
 * Time: 18:42:33
 * </pre>
 */
public class PTest extends TestCase {

    public void testApplyAttributes() throws Exception {

        DomModel model = new DomModel();

        //1
        P p = new P();
        p.setModel(model);
        p.setContent("foo");

        JComponent jComponent = DomConverter.convertComponent(p);
        assertTrue(jComponent instanceof JLabel);
        JLabel l = (JLabel) jComponent;
        assertEquals("foo", l.getText());

        //2
        p = new P();
        p.setModel(model);
        p.setContent("foo");
        p.setAttribute(P.CONTENT_ATTRIBUTE, "html");
        p.setComponent(p.createComponent());
        assertTrue(p.getComponent() instanceof JLabel);

        jComponent = DomConverter.convertComponent(p);
        assertTrue(jComponent instanceof JLabel);
        l = (JLabel) jComponent;
        assertEquals("<html>foo</html>", l.getText());

        //3
        p = new P();
        p.setModel(model);
        p.setContent("foo");
        p.setAttribute(P.CONTENT_ATTRIBUTE, "text");
        p.setComponent(p.createComponent());
        assertTrue(p.getComponent() instanceof JLabel);

        jComponent = DomConverter.convertComponent(p);
        assertTrue(jComponent instanceof JLabel);
        l = (JLabel) jComponent;
        assertEquals("foo", l.getText());


        //4 - resolving placeholders
        String html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:c=\"http://www.oracle.com/swing\"\n" +
                        "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <p>Hello ${name}</p>" +
                "</body>\n" +
                "</html>";
        model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));

        model.addModelElement("name", "world");
        JComponent root = DomConverter.toSwing(model);
        assertNotNull(root);

        assertEquals(1, root.getComponentCount());
        JLabel label = (JLabel) root.getComponent(0);
        assertEquals("Hello world", label.getText());

    }


    public void testBinding() throws Exception {

        //4 - resolving placeholders
        String html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:c=\"http://www.oracle.com/swing\"\n" +
                        "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <p content='el'>${name}</p>" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));

        model.addModelElement("name", "Hello world");
        JComponent root = DomConverter.toSwing(model);
        assertNotNull(root);

        assertEquals(1, root.getComponentCount());
        JLabel label = (JLabel) root.getComponent(0);
        assertEquals("Hello world", label.getText());

        model.addModelElement("name", "Hello world1");
        model.rebindModelElement("name");
        assertEquals("Hello world1", label.getText());

    }


}
