package ru.swing.html.tags;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomModel;

import javax.swing.*;

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

    }
}
