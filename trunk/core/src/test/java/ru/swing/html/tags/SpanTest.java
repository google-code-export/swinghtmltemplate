package ru.swing.html.tags;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomModel;

import javax.swing.*;
import java.awt.*;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 24.11.2010
 * Time: 11:28:45
 * </pre>
 */
public class SpanTest extends TestCase {

    public void testConvertsToJPanel() throws Exception {
        DomModel model = new DomModel();

        Span tag = new Span();
        tag.setModel(model);

        JComponent jComponent = DomConverter.convertComponent(tag);
        assertNotNull(jComponent);
        assertEquals(JPanel.class, jComponent.getClass());


    }

    public void testDefaultLayout() throws Exception {
        DomModel model = new DomModel();

        Span tag = new Span();
        tag.setModel(model);

        JComponent jComponent = DomConverter.convertComponent(tag);
        assertNotNull(jComponent.getLayout());
        assertEquals(FlowLayout.class, jComponent.getLayout().getClass());


    }


}
