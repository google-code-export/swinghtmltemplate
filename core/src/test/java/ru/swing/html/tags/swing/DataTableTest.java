package ru.swing.html.tags.swing;

import junit.framework.TestCase;
import org.jdesktop.observablecollections.ObservableCollections;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.lang.*;
import java.lang.Object;
import java.util.*;

/**
 * Tests DataTable tag.
 */
public class DataTableTest extends TestCase {

    public void testBindingValue() throws Exception {

        String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                "      xmlns:c=\"http://www.oracle.com/swing\">" +
                "<body>" +
                "<c:dataTable id='ttt' value='${model.items}'>" +
                "<c:column value='${name}'/>" +
                "<c:column value='${age}'/>" +
                "</c:dataTable>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));


        Model m = new Model();
        Person person = new Person();
        person.setName("name1");
        person.setAge(29);
        m.getItems().add(person);

        person = new Person();
        person.setName("name2");
        person.setAge(11);
        m.getItems().add(person);

        model.addModelElement("model", m);

        DomConverter.toSwing(model);
        JTable table = (JTable) model.getTagById("ttt").getComponent();

        //check table has 2 rows
        assertEquals(2, table.getModel().getRowCount());

        //check table has 2 columns
        assertEquals(2, table.getColumnCount());

        //check table returns correct data
        assertEquals("name1", table.getModel().getValueAt(0, 0));
        assertEquals(29, table.getModel().getValueAt(0, 1));
        assertEquals("name2", table.getModel().getValueAt(1, 0));
        assertEquals(11, table.getModel().getValueAt(1, 1));

        //check binding updates values
        //add new person
        m.getItems().add(new Person());
        assertEquals(m.getItems().size(), table.getModel().getRowCount());
        //modify data
        m.getItems().get(0).setName("name1 updated");
        assertEquals(m.getItems().get(0).getName(), table.getModel().getValueAt(0, 0));

    }








    public class Model {

        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
        private java.util.List<Person> items = ObservableCollections.observableList(new ArrayList<Person>());
        private Person selectedPerson;
        private java.util.List<Person> selectedPersons;

        public java.util.List<Person> getItems() {
            return items;
        }

        public void setItems(java.util.List<Person> items) {
            this.items = items;
        }

        public Person getSelectedPerson() {
            return selectedPerson;
        }

        public void setSelectedPerson(Person selectedPerson) {
            Object old = this.selectedPerson;
            this.selectedPerson = selectedPerson;
            pcs.firePropertyChange("selectedPerson", old, selectedPerson);
        }

        public java.util.List<Person> getSelectedPersons() {
            return selectedPersons;
        }

        public void setSelectedPersons(java.util.List<Person> selectedPersons) {
            this.selectedPersons = selectedPersons;
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }

    }


    public class Person {
        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
        private String name;
        private int age;
        private boolean active;
        private Color color = Color.BLACK;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            java.lang.Object old = this.name;
            this.name = name;
            pcs.firePropertyChange("name", old, name);
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            int old = this.age;
            this.age = age;
            pcs.firePropertyChange("age", old, age);
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            boolean old = this.active;
            this.active = active;
            pcs.firePropertyChange("active", old, active);
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            java.lang.Object old = this.name;
            this.color = color;
            pcs.firePropertyChange("color", old, color);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", active=" + active +
                    ", color=" + color +
                    '}';
        }
    }

}
