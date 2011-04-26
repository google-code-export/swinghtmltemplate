package ru.swing.html.tags;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomModel;

import javax.swing.*;
import java.awt.*;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 23.11.2010
 * Time: 18:47:22
 * </pre>
 */
public class DivTest extends TestCase {

    public void testConvertsToJPanel() throws Exception {

        Div tag = new Div();

        JComponent jComponent = DomConverter.convertComponent(tag);
        assertNotNull(jComponent);
        assertEquals(JPanel.class, jComponent.getClass());


    }

    public void testDefaultLayout() throws Exception {
        DomModel model = new DomModel();

        Div tag = new Div();
        tag.setModel(model);

        JComponent jComponent = DomConverter.convertComponent(tag);
        assertNotNull(jComponent.getLayout());
        assertEquals(BorderLayout.class, jComponent.getLayout().getClass());


    }
}
