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

        String html = "<html>" +
                "<head></head>" +
                "<body style='display: border;'>" +
                "   <scroll>" +
                "      <textarea/>" +
                "      <object type='javax.swing.JTextPane'/>" +
                "   </scroll>" +
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
}