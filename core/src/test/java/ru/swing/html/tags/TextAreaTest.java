package ru.swing.html.tags;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 24.11.2010
 * Time: 11:36:09
 * </pre>
 */
public class TextAreaTest extends TestCase {

    public void testCreateComponent() throws Exception {

        TextArea tag = new TextArea();
        assertEquals(JTextArea.class, DomConverter.convertComponent(tag).getClass());


    }


    public void testSetContent() throws Exception {

        String html = "<html>" +
                "<head></head>" +
                "<body style='display: border;'>" +
                "   <textarea>Foo</textarea>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        JComponent root = model.getRootTag().getChildByName("body").getComponent();


        assertEquals(1, root.getComponentCount());

        assertEquals(BorderLayout.class, root.getLayout().getClass());
        BorderLayout l = (BorderLayout) root.getLayout();

        assertTrue(l.getLayoutComponent(root, BorderLayout.CENTER) instanceof JTextArea);
        JTextArea label = (JTextArea) l.getLayoutComponent(root, BorderLayout.CENTER);
        assertEquals("Foo", label.getText());

    }
}
