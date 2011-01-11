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
public class LabelTest extends TestCase {

    public void testApplyAttributes() throws Exception {

        DomModel model = new DomModel();

        //1
        Label p = new Label();
        p.setModel(model);
        p.setContent("foo");

        JComponent jComponent = DomConverter.convertComponent(p);
        assertTrue(jComponent instanceof JLabel);
        JLabel l = (JLabel) jComponent;
        assertEquals("foo", l.getText());


    }

}
