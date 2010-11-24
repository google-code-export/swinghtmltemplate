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
 * Time: 11:21:20
 * </pre>
 */
public class InputTest extends TestCase {

    public void testCreateComponent() throws Exception {

        Input tag = new Input();
        assertEquals(JTextField.class, DomConverter.convertComponent(tag).getClass());

        tag = new Input();
        tag.setAttribute("type", "text");
        assertEquals(JTextField.class, DomConverter.convertComponent(tag).getClass());

        tag = new Input();
        tag.setAttribute("type", "password");
        assertEquals(JPasswordField.class, DomConverter.convertComponent(tag).getClass());

        tag = new Input();
        tag.setAttribute("type", "button");
        assertEquals(JButton.class, DomConverter.convertComponent(tag).getClass());

        tag = new Input();
        tag.setAttribute("type", "checkbox");
        assertEquals(JCheckBox.class, DomConverter.convertComponent(tag).getClass());

        tag = new Input();
        tag.setAttribute("type", "radio");
        assertEquals(JRadioButton.class, DomConverter.convertComponent(tag).getClass());

    }


    public void testSetContent() throws Exception {

        String html = "<html>" +
                "<head></head>" +
                "<body style='display: border;'>" +
                "   <input>Foo</input>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        JComponent root = model.getRootTag().getChildByName("body").getComponent();


        assertEquals(1, root.getComponentCount());

        assertEquals(BorderLayout.class, root.getLayout().getClass());
        BorderLayout l = (BorderLayout) root.getLayout();

        assertTrue(l.getLayoutComponent(root, BorderLayout.CENTER) instanceof JTextField);
        JTextField label = (JTextField) l.getLayoutComponent(root, BorderLayout.CENTER);
        assertEquals("Foo", label.getText());


    }
}
