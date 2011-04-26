package ru.swing.html.tags;

import junit.framework.TestCase;
import net.miginfocom.swing.MigLayout;
import ru.swing.html.DomConverter;

import javax.swing.*;
import java.awt.*;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 24.11.2010
 * Time: 10:57:02
 * </pre>
 */
public class FormTest extends TestCase {

    public void testConvertsToJPanel() throws Exception {

        Form tag = new Form();
        JComponent jComponent = DomConverter.convertComponent(tag);
        assertNotNull(jComponent);
        assertEquals(JPanel.class, jComponent.getClass());


    }


    public void testDefaultLayout() throws Exception {

        Form tag = new Form();
        JComponent jComponent = DomConverter.convertComponent(tag);
        assertNotNull(jComponent.getLayout());
        assertEquals(MigLayout.class, jComponent.getLayout().getClass());


    }

}
