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
 * Date: 23.11.2010
 * Time: 18:11:07
 * </pre>
 */
public class BorderLayoutTest extends TestCase {

    public void testBorderLayout() throws Exception {

        String html = "<html>\n" +
                "<head></head>\n" +
                "<body style='display: border;'>\n" +
                "   <p content='html'>center</p>\n" +
                "   <p align='top'>top</p>\n" +
                "   <p align='bottom' content='html'><![CDATA[<i>bottom</i>]]></p>\n" +
                "   <p align='left'>left</p>\n" +
                "   <p align='right'>right</p>\n" +
                "</body>\n" +
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
