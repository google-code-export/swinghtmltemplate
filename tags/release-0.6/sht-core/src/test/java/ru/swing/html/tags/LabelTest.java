package ru.swing.html.tags;

import junit.framework.TestCase;
import org.jdom.JDOMException;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

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

    public void testForAttr() throws Exception {
        String html =
                "<html>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <label id='lbl' for='btn'>Label</label>" +
                "   <input id='btn' type='button' onclick='foo'/>" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);

        JButton btn = (JButton) model.getTagById("btn").getComponent();
        JLabel lbl = (JLabel) model.getTagById("lbl").getComponent();
        assertEquals(btn, lbl.getLabelFor());

    }

}
