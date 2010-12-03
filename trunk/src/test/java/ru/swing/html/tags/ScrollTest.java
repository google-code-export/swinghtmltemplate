package ru.swing.html.tags;

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
 * Date: 24.11.2010
 * Time: 11:29:30
 * </pre>
 */
public class ScrollTest extends TestCase {

    public void testConvertsToJPanel() throws Exception {
        DomModel model = new DomModel();

        ScrollPane tag = new ScrollPane();
        tag.setModel(model);

        JComponent jComponent = DomConverter.convertComponent(tag);
        assertNotNull(jComponent);
        assertEquals(JScrollPane.class, jComponent.getClass());


    }


    public void testChildInViewport() throws Exception {

        String html = "<html xmlns:c='http://www.oracle.com/swing'>" +
                "<head></head>" +
                "<body style='display: border;'>" +
                "   <c:scroll>" +
                "      <textarea/>" +
                "      <object classid='javax.swing.JTextPane'/>" +
                "   </c:scroll>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        JComponent root = model.getRootTag().getChildByName("body").getComponent();


        assertEquals(BorderLayout.class, root.getLayout().getClass());
        BorderLayout l = (BorderLayout) root.getLayout();

        //центральный компонент
        assertTrue(l.getLayoutComponent(root, BorderLayout.CENTER) instanceof JScrollPane);
        JScrollPane scroll = (JScrollPane) l.getLayoutComponent(root, BorderLayout.CENTER);
        assertEquals(JTextArea.class, scroll.getViewport().getComponent(0).getClass());


    }
    public void testAttributes() throws Exception {

        String html = "<html xmlns:c='http://www.oracle.com/swing'>" +
                "<head></head>" +
                "<body style='display: border;'>" +
                "   <c:scroll>" +
                "      <c:attribute name='bounds' value='10 10 10 10' type='java.awt.Rectangle'/>" +
                "      <textarea/>" +
                "   </c:scroll>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        JComponent root = model.getRootTag().getChildByName("body").getComponent();


        assertEquals(BorderLayout.class, root.getLayout().getClass());
        BorderLayout l = (BorderLayout) root.getLayout();

        //центральный компонент
        assertTrue(l.getLayoutComponent(root, BorderLayout.CENTER) instanceof JScrollPane);
        JScrollPane scroll = (JScrollPane) l.getLayoutComponent(root, BorderLayout.CENTER);
        assertEquals(new Rectangle(10, 10, 10, 10), scroll.getBounds());
        assertEquals(JTextArea.class, scroll.getViewport().getComponent(0).getClass());


    }
}
