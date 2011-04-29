package ru.swing.html.tags.ui;

import junit.framework.TestCase;
import org.jdesktop.observablecollections.ObservableCollections;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;

public class ForEachTest extends TestCase {

    public void testIterations() throws Exception {

        String html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:c=\"http://www.oracle.com/swing\"\n" +
                        "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <ui:forEach items='${items}' var='i'>" +
                "      <p>${i}</p>" +
                "   </ui:forEach>" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));

        model.addModelElement("items", Arrays.asList("1", "2", "3"));
        JComponent root = DomConverter.toSwing(model);
        assertNotNull(root);

        assertEquals(3, root.getComponentCount());
        //this doesn't work because el means binding, so all labels will have text "3"
        assertEquals(JLabel.class, root.getComponent(0).getClass());
        assertEquals("1", ((JLabel)root.getComponent(0)).getText());
        assertEquals(JLabel.class, root.getComponent(1).getClass());
        assertEquals("2", ((JLabel)root.getComponent(1)).getText());
        assertEquals(JLabel.class, root.getComponent(2).getClass());
        assertEquals("3", ((JLabel)root.getComponent(2)).getText());

    }

    public void testIterationsBinding() throws Exception {

        String html =
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:c=\"http://www.oracle.com/swing\"\n" +
                        "      xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "   <ui:forEach items='${items}' var='i' varStatus='s'>" +
                "      <p id='${s.index}' content='el'>${prefix} ${i.name}</p>" +
                "   </ui:forEach>" +
                "</body>\n" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));

        List<Person> elements = ObservableCollections.observableList(Arrays.asList(new Person("1"), new Person("2"), new Person("3")));
        model.addModelElement("prefix", "My name is");
        model.addModelElement("items", elements);
        JComponent root = DomConverter.toSwing(model);
        assertNotNull(root);

        assertEquals(3, root.getComponentCount());
        //this doesn't work because el means binding, so all labels will have text "3"
        assertEquals(JLabel.class, root.getComponent(0).getClass());
        assertEquals("My name is 1", ((JLabel)root.getComponent(0)).getText());
        assertEquals(JLabel.class, root.getComponent(1).getClass());
        assertEquals("My name is 2", ((JLabel)root.getComponent(1)).getText());
        assertEquals(JLabel.class, root.getComponent(2).getClass());
        assertEquals("My name is 3", ((JLabel)root.getComponent(2)).getText());

        elements.get(0).setName("4");
        assertEquals("My name is 4", ((JLabel) root.getComponent(0)).getText());

        model.addModelElement("prefix", "Hello");
        assertEquals("Hello 2", ((JLabel) root.getComponent(1)).getText());
        assertEquals("Hello 3", ((JLabel) root.getComponent(2)).getText());
        assertEquals("Hello 4", ((JLabel) root.getComponent(0)).getText());



    }



    public class Person {

        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

        private String name;

        public Person() {
        }

        public Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            Object old = this.name;
            this.name = name;
            pcs.firePropertyChange("name", old, name);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }
    }

}
