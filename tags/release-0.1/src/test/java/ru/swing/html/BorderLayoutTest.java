package ru.swing.html;

import junit.framework.TestCase;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 23.11.2010
 * Time: 18:11:07
 * </pre>
 */
public class BorderLayoutTest extends TestCase {

    public void testBorderLayout() throws Exception {

        String html = "<html>" +
                "<head></head>" +
                "<body style='display: border;'>" +
                "   <p content='html'>center</p>" +
                "   <p align='top'>top</p>" +
                "   <p align='bottom' content='html'><![CDATA[<i>bottom</i>]]></p>" +
                "   <p align='left'>left</p>" +
                "   <p align='right'>right</p>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        JComponent root = model.getRootTag().getChildByName("body").getComponent();

        assertEquals(5, root.getComponentCount());

        assertEquals(BorderLayout.class, root.getLayout().getClass());
        BorderLayout l = (BorderLayout) root.getLayout();

        //центральный компонент
        assertTrue(l.getLayoutComponent(root, BorderLayout.CENTER) instanceof JLabel);
        JLabel label = (JLabel) l.getLayoutComponent(root, BorderLayout.CENTER);
        assertEquals("<html>center</html>", label.getText());

        //верхний компонент
        assertTrue(l.getLayoutComponent(root, BorderLayout.NORTH) instanceof JLabel);
        label = (JLabel) l.getLayoutComponent(root, BorderLayout.NORTH);
        assertEquals("top", label.getText());

        //левый компонент
        assertTrue(l.getLayoutComponent(root, BorderLayout.WEST) instanceof JLabel);
        label = (JLabel) l.getLayoutComponent(root, BorderLayout.WEST);
        assertEquals("left", label.getText());

        //нижний компонент
        assertTrue(l.getLayoutComponent(root, BorderLayout.SOUTH) instanceof JLabel);
        label = (JLabel) l.getLayoutComponent(root, BorderLayout.SOUTH);
        assertEquals("<html><i>bottom</i></html>", label.getText());

        //правый компонент
        assertTrue(l.getLayoutComponent(root, BorderLayout.EAST) instanceof JLabel);
        label = (JLabel) l.getLayoutComponent(root, BorderLayout.EAST);
        assertEquals("right", label.getText());



    }
}
