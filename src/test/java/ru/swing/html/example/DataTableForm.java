package ru.swing.html.example;

import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;
import org.jdom.JDOMException;
import ru.swing.html.Binder;
import ru.swing.html.DomModel;
import ru.swing.html.ModelElement;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 12.04.11
 * Time: 16:59
 */
public class DataTableForm extends JPanel {

    private DomModel domModel;

    @ModelElement("model")
    private Model model = new Model();
    
    public DataTableForm() {
        try {
            domModel = Binder.bind(this, true);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        DataTableForm form = new DataTableForm();

        JFrame f = new JFrame("Test");
        f.setSize(400, 200);

        f.getContentPane().add(form);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public void onClick() {
        Person person = new Person();
        person.setAge(new Random().nextInt(80));
        person.setName("Foo");
        model.getItems().add(person);
    }

    public void onClick2() {
        for (Person p : model.getItems()) {
            p.setAge(new Random().nextInt(80));
        }
    }

    public class Model {
        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
        private List<Person> items = ObservableCollections.observableList(new ArrayList<Person>());

        public List<Person> getItems() {
            return items;
        }

        public void setItems(List<Person> items) {
            this.items = items;
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
            Object old = this.name;
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
            Object old = this.name;
            this.color = color;
            pcs.firePropertyChange("color", old, color);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }
    }
}
