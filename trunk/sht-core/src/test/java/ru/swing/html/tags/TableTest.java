package ru.swing.html.tags;

import info.clearthought.layout.TableLayout;
import junit.framework.TestCase;
import org.jdom.JDOMException;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

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

        //central component
        assertTrue(l.getLayoutComponent(root, BorderLayout.CENTER) instanceof JPanel);
        JPanel table = (JPanel) l.getLayoutComponent(root, BorderLayout.CENTER);

        assertEquals(TableLayout.class, table.getLayout().getClass());
        TableLayout layout = (TableLayout) table.getLayout();

        assertEquals(1, layout.getNumColumn());
        assertEquals(1, layout.getNumRow());


    }


    public void testColumnsSizes() throws Exception {
        String html = "<html>" +
                "<head></head>" +
                "<body style='display: border;'>" +
                "   <table>" +
                "   <tr>" +
                "      <td width='10'><p>Foo</p></td>" +
                "      <td width='fill'><p>Foo</p></td>" +
                "      <td width='preferred'><p>Foo</p></td>" +
                "      <td width='0.13'><p>Foo</p></td>" +
                "      <td width='16%'><p>Foo</p></td>" +
                "   </tr>" +
                "   <tr>" +
                "      <td width='8'><p>Foo</p></td>" +
                "      <td width='fill'><p>Foo</p></td>" +
                "      <td width='preferred'><p>Foo</p></td>" +
                "      <td width='0.14'><p>Foo</p></td>" +
                "      <td width='16%'><p>Foo</p></td>" +
                "   </tr>" +
                "   </table>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        JComponent root = model.getRootTag().getChildByName("body").getComponent();
        BorderLayout l = (BorderLayout) root.getLayout();


        //central component
        assertTrue(l.getLayoutComponent(root, BorderLayout.CENTER) instanceof JPanel);
        JPanel table = (JPanel) l.getLayoutComponent(root, BorderLayout.CENTER);

        assertEquals(TableLayout.class, table.getLayout().getClass());
        TableLayout layout = (TableLayout) table.getLayout();

        assertEquals(5, layout.getNumColumn());
        assertEquals(10.0, layout.getColumn(0));
        assertEquals(TableLayout.FILL, layout.getColumn(1));
        assertEquals(TableLayout.PREFERRED, layout.getColumn(2));
        assertEquals(0.14, layout.getColumn(3));
        assertEquals(0.16, layout.getColumn(4));
    }

    public void testRowsSizes() throws Exception {
        String html = "<html>" +
                "<head></head>" +
                "<body style='display: border;'>" +
                "   <table>" +
                "   <tr>" +
                "      <td height='10'><p>Foo</p></td>" +
                "      <td height='12'><p>Foo</p></td>" +
                "   </tr>" +
                "   <tr>" +
                "      <td height='preferred'><p>Foo</p></td>" +
                "      <td height='preferred'><p>Foo</p></td>" +
                "   </tr>" +
                "   <tr>" +
                "      <td height='fill'><p>Foo</p></td>" +
                "      <td height='fill'><p>Foo</p></td>" +
                "   </tr>" +
                "   <tr>" +
                "      <td height='0.25'><p>Foo</p></td>" +
                "      <td height='0.21'><p>Foo</p></td>" +
                "   </tr>" +
                "   <tr>" +
                "      <td height='14%'><p>Foo</p></td>" +
                "      <td height='16%'><p>Foo</p></td>" +
                "   </tr>" +
                "   </table>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        JComponent root = model.getRootTag().getChildByName("body").getComponent();
        BorderLayout l = (BorderLayout) root.getLayout();


        //central component
        assertTrue(l.getLayoutComponent(root, BorderLayout.CENTER) instanceof JPanel);
        JPanel table = (JPanel) l.getLayoutComponent(root, BorderLayout.CENTER);

        assertEquals(TableLayout.class, table.getLayout().getClass());
        TableLayout layout = (TableLayout) table.getLayout();

        assertEquals(5, layout.getNumRow());
        assertEquals(12.0, layout.getRow(0));
        assertEquals(TableLayout.PREFERRED, layout.getRow(1));
        assertEquals(TableLayout.FILL, layout.getRow(2));
        assertEquals(0.25, layout.getRow(3));
        assertEquals(0.16, layout.getRow(4));
    }
}
