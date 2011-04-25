package ru.swing.html.tags;

import info.clearthought.layout.TableLayout;
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
 * Time: 11:59:36
 * </pre>
 */
public class TableTest extends TestCase {

    public void testConvertsToJPanel() throws Exception {

        Table tag = new Table();

        JComponent jComponent = DomConverter.convertComponent(tag);
        assertNotNull(jComponent);
        assertEquals(JPanel.class, jComponent.getClass());


    }

    public void testLayout() throws Exception {

        //1
        Table tag = new Table();

        JComponent jComponent = DomConverter.convertComponent(tag);
        assertNotNull(jComponent.getLayout());
        assertEquals(TableLayout.class, jComponent.getLayout().getClass());

        //2
        tag = new Table();
        tag.setAttribute("display", "flow");

        jComponent = DomConverter.convertComponent(tag);
        assertNotNull(jComponent.getLayout());
        assertEquals(TableLayout.class, jComponent.getLayout().getClass());


    }


    public void testCells() throws Exception {

        String html = "<html>" +
                "<head></head>" +
                "<body style='display: border;'>" +
                "   <table>" +
                "   <tr>" +
                "      <td><p>Foo</p></td>" +
                "   </tr>" +
                "   </table>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        JComponent root = model.getRootTag().getChildByName("body").getComponent();

        assertEquals(1, root.getComponentCount());

        assertEquals(BorderLayout.class, root.getLayout().getClass());
        BorderLayout l = (BorderLayout) root.getLayout();

        //центральный компонент
        assertTrue(l.getLayoutComponent(root, BorderLayout.CENTER) instanceof JPanel);
        JPanel table = (JPanel) l.getLayoutComponent(root, BorderLayout.CENTER);

        assertEquals(TableLayout.class, table.getLayout().getClass());
        TableLayout layout = (TableLayout) table.getLayout();

        assertEquals(1, layout.getNumColumn());
        assertEquals(1, layout.getNumRow());


    }
}
