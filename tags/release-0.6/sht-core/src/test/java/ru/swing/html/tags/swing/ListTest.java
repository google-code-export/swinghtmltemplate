package ru.swing.html.tags.swing;

import junit.framework.TestCase;
import ru.swing.html.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class ListTest extends TestCase {

    public void testCreateComponent() throws Exception {
        List list = new List();
        JComponent c = list.createComponent();
        assertNotNull(c);
        assertTrue(c instanceof JList);
    }

    public void testType() throws Exception {
        List list = new List();
        JList c = (JList) list.createComponent();
        
        assertEquals(JList.VERTICAL, c.getLayoutOrientation());

        list.setAttribute("type", "vertical-wrap");
        list.applyAttribute(c, "type");
        assertEquals(JList.VERTICAL_WRAP, c.getLayoutOrientation());

        list.setAttribute("type", "horizontal-wrap");
        list.applyAttribute(c, "type");
        assertEquals(JList.HORIZONTAL_WRAP, c.getLayoutOrientation());

    }

    public void testRowsPerColumn() throws Exception {
        List list = new List();
        JList c = (JList) list.createComponent();

        list.setAttribute("rowsPerColumn", "2");
        list.applyAttribute(c, "rowsPerColumn");
        assertEquals(2, c.getVisibleRowCount());

        list.setAttribute("rowsPerColumn", "1");
        list.applyAttribute(c, "rowsPerColumn");
        assertEquals(1, c.getVisibleRowCount());
    }

    public void testOnchange() throws Exception {

        TestListChange controller = new TestListChange();
        String html =
                "<html xmlns:j=\"http://www.oracle.com/swing\">\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <j:list id='b' onchange='foo'>\n" +
                "       <j:selectItems value=\"${items}\" />\n" +
                "   </j:list>" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);

        assertNull(controller.text);

        Binder.bind(controller, false, model);
        JList b = (JList) model.getTagById("b").getComponent();

        b.setSelectedIndex(0);
        assertEquals("foo1", controller.text);
        assertEquals(1, controller.selectedCount);

        b.setSelectedIndex(1);
        assertEquals("foo2", controller.text);
        assertEquals(1, controller.selectedCount);

        b.setSelectedIndices(new int[]{0, 1});
        assertEquals(2, controller.selectedCount);

    }

    public class TestListChange {

        public String text = null;
        public int selectedCount = 0;

        @Bind("b")
        private JList list;

        @ModelElement("items")
        private java.util.List<String> items = Arrays.asList("foo1", "foo2");

        public void foo(ListSelectionEvent e) {
            text = (String) list.getSelectedValue();
            selectedCount = list.getSelectedIndices().length;
        }
    }


}
