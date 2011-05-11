package ru.swing.html.tags.swing;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;
import ru.swing.html.tags.swing.Tabs;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 24.11.2010
 * Time: 11:39:10
 * </pre>
 */
public class TabsTest extends TestCase {


    public void testConvertsToJPanel() throws Exception {

        Tabs tag = new Tabs();
        JComponent jComponent = DomConverter.convertComponent(tag);
        assertNotNull(jComponent);
        assertEquals(JTabbedPane.class, jComponent.getClass());


    }

    public void testChildren() throws Exception {
        String html = "<html xmlns:c='http://www.oracle.com/swing'>" +
                "<head></head>" +
                "<body style='display: border;'>" +
                "   <c:tabs>" +
                "      <div title='tab1'></div>" +
                "      <span title='tab2'></span>" +
                "   </c:tabs>" +
                "</body>" +
                "</html>";

        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        JComponent root = model.getRootTag().getChildByName("body").getComponent();

        assertEquals(1, root.getComponentCount());

        assertEquals(BorderLayout.class, root.getLayout().getClass());
        BorderLayout l = (BorderLayout) root.getLayout();

        //центральный компонент
        assertTrue(l.getLayoutComponent(root, BorderLayout.CENTER) instanceof JTabbedPane);
        JTabbedPane tabs = (JTabbedPane) l.getLayoutComponent(root, BorderLayout.CENTER);


        assertEquals(2, tabs.getTabCount());

        assertEquals("tab1", tabs.getTitleAt(0));
        assertEquals(JPanel.class, tabs.getComponentAt(0).getClass());
        assertEquals(BorderLayout.class, ((JComponent)tabs.getComponentAt(0)).getLayout().getClass());

        assertEquals("tab2", tabs.getTitleAt(1));
        assertEquals(JPanel.class, tabs.getComponentAt(1).getClass());
        assertEquals(FlowLayout.class, ((JComponent)tabs.getComponentAt(1)).getLayout().getClass());

    }

    public void testChildrenAndAttribute() throws Exception {
        String html = "<html xmlns:c='http://www.oracle.com/swing'>" +
                "<head></head>" +
                "<body style='display: border;'>" +
                "   <c:tabs>" +
                "      <div title='tab1'></div>" +
                "      <span title='tab2'></span>" +
                "      <c:attribute name='selectedIndex' value='1' type='int'/>" +
                "   </c:tabs>" +
                "</body>" +
                "</html>";

        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        JComponent root = model.getRootTag().getChildByName("body").getComponent();

        assertEquals(1, root.getComponentCount());

        assertEquals(BorderLayout.class, root.getLayout().getClass());
        BorderLayout l = (BorderLayout) root.getLayout();

        //центральный компонент
        assertTrue(l.getLayoutComponent(root, BorderLayout.CENTER) instanceof JTabbedPane);
        JTabbedPane tabs = (JTabbedPane) l.getLayoutComponent(root, BorderLayout.CENTER);


        assertEquals(2, tabs.getTabCount());

        assertEquals("tab1", tabs.getTitleAt(0));
        assertEquals(JPanel.class, tabs.getComponentAt(0).getClass());
        assertEquals(BorderLayout.class, ((JComponent)tabs.getComponentAt(0)).getLayout().getClass());

        assertEquals("tab2", tabs.getTitleAt(1));
        assertEquals(JPanel.class, tabs.getComponentAt(1).getClass());
        assertEquals(FlowLayout.class, ((JComponent)tabs.getComponentAt(1)).getLayout().getClass());

        assertEquals(1, tabs.getSelectedIndex());

    }


    public void testTabPosition() throws Exception {

        Tabs tag = new Tabs();
        tag.setAttribute(Tabs.TAB_POSITION_ATTRIBUTE, "left");
        JTabbedPane jComponent = (JTabbedPane) DomConverter.convertComponent(tag);
        assertEquals(JTabbedPane.LEFT, jComponent.getTabPlacement());

        tag = new Tabs();
        tag.setAttribute(Tabs.TAB_POSITION_ATTRIBUTE, "right");
        jComponent = (JTabbedPane) DomConverter.convertComponent(tag);
        assertEquals(JTabbedPane.RIGHT, jComponent.getTabPlacement());

        tag = new Tabs();
        tag.setAttribute(Tabs.TAB_POSITION_ATTRIBUTE, "top");
        jComponent = (JTabbedPane) DomConverter.convertComponent(tag);
        assertEquals(JTabbedPane.TOP, jComponent.getTabPlacement());

        tag = new Tabs();
        tag.setAttribute(Tabs.TAB_POSITION_ATTRIBUTE, "bottom");
        jComponent = (JTabbedPane) DomConverter.convertComponent(tag);
        assertEquals(JTabbedPane.BOTTOM, jComponent.getTabPlacement());


    }

}
