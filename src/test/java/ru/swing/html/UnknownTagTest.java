package ru.swing.html;

import junit.framework.TestCase;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 24.11.2010
 * Time: 11:09:31
 * </pre>
 */
public class UnknownTagTest extends TestCase {

    public void testParsingOfUnknownTest() throws Exception {

        String html = "<html>" +
                "<head></head>" +
                "<body style='display: border;'>" +
                "   <ul>" +
                "      <li><p content='html'>center</p></li>" +
                "      <li><p align='top'>top</p></li>" +
                "      <li><p align='bottom' content='html'><![CDATA[<i>bottom</i>]]></p></li>" +
                "      <li><p align='left'>left</p></li>" +
                "      <li><p align='right'>right</p></li>" +
                "   </ul>" +
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
