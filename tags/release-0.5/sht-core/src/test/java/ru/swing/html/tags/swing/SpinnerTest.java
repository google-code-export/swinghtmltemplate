package ru.swing.html.tags.swing;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;
import ru.swing.html.tags.Tag;
import ru.swing.html.tags.swing.Spinner;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;

/**
 * Tests Spinner tag.
 */
public class SpinnerTest extends TestCase {

    public void testCreateComponent() throws Exception {

        Spinner tag = new Spinner();
        assertEquals(JSpinner.class, DomConverter.convertComponent(tag).getClass());

        tag = new Spinner();
        tag.setAttribute(Tag.TAG_CONTENT, "10");
        assertEquals(10, ((JSpinner) DomConverter.convertComponent(tag)).getValue());

    }

    
    public void testSetContent() throws Exception {

        String html = "<html xmlns:c='http://www.oracle.com/swing'>" +
                "<head></head>" +
                "<body style='display: border;'>" +
                "   <c:spinner>10</c:spinner>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        JComponent root = model.getRootTag().getChildByName("body").getComponent();


        assertEquals(1, root.getComponentCount());

        assertEquals(BorderLayout.class, root.getLayout().getClass());
        BorderLayout l = (BorderLayout) root.getLayout();

        assertTrue(l.getLayoutComponent(root, BorderLayout.CENTER) instanceof JSpinner);
        JSpinner spinner = (JSpinner) l.getLayoutComponent(root, BorderLayout.CENTER);
        assertEquals(10, spinner.getValue());


    }



}
