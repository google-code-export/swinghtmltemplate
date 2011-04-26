package ru.swing.html.layout;

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
 * Date: 26.11.2010
 * Time: 10:34:55
 * </pre>
 */
public class AbsoluteLayoutTest extends TestCase {

    public void testLayout() throws Exception {


        String html = "<html xmlns:c='http://www.oracle.com/swing'>" +
                "<head></head>" +
                "<body style='display: absolute;'>" +
                "   <p content='html'>" +
                "      <c:attribute name='bounds' value='10 15 25 40' type='java.awt.Rectangle'/>" +
                "      1" +
                "    </p>" +
                "   <p align='45 15 25 40'>2</p>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        JComponent root = model.getRootTag().getChildByName("body").getComponent();

        assertEquals(2, root.getComponentCount());
        assertNull(root.getLayout());

        assertEquals(new Rectangle(10, 15, 25, 40), root.getComponent(0).getBounds());
        assertEquals(new Rectangle(45, 15, 25, 40), root.getComponent(1).getBounds());



    }
}
