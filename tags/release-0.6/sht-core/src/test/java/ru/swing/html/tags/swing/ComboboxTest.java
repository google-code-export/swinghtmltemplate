package ru.swing.html.tags.swing;

import junit.framework.TestCase;
import ru.swing.html.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class ComboboxTest extends TestCase {

    public void testCreateComponent() throws Exception {
        Combobox combobox = new Combobox();
        JComponent c = combobox.createComponent();
        assertNotNull(c);
        assertTrue(c instanceof JComboBox);
    }


    public void testOnchange() throws Exception {

        TestComboboxChange controller = new TestComboboxChange();
        String html =
                "<html xmlns:j=\"http://www.oracle.com/swing\">\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <j:combobox id='b' onchange='foo'>\n" +
                "       <j:selectItems value=\"${items}\" />\n" +
                "   </j:combobox>" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);

        assertNull(controller.text);

        Binder.bind(controller, false, model);
        JComboBox b = (JComboBox) model.getTagById("b").getComponent();

        b.setSelectedIndex(0);
        assertEquals("foo1", controller.text);

        b.setSelectedIndex(1);
        assertEquals("foo2", controller.text);

    }

    public class TestComboboxChange {

        public String text = null;

        @ModelElement("items")
        private java.util.List<String> items = Arrays.asList("foo1", "foo2");

        public void foo(ActionEvent e) {
            JComboBox b = (JComboBox) e.getSource();
            text = (String) b.getSelectedItem();
        }
    }
}
