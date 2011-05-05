package ru.swing.html.example;

import org.jdom.JDOMException;
import ru.swing.html.Binder;
import ru.swing.html.DomModel;
import ru.swing.html.ModelElement;
import ru.swing.html.example.utils.*;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 13.04.11
 * Time: 13:24
 */
public class FormTableForm extends JPanel {

    private DomModel domModel;

    @ModelElement("person")
    private Person person;

    @ModelElement("colorEditor")
    private ColorTableCellEditor colorEditor = new ColorTableCellEditor();

    @ModelElement("spinnerEditor")
    private SpinnerEditor spinnerEditor = new SpinnerEditor();

    @ModelElement("booleanEditor")
    private BooleanEditor checkboxEditor = new BooleanEditor();

    @ModelElement("colorRenderer")
    private ColorTableCellRenderer colorRenderer = new ColorTableCellRenderer();

    @ModelElement("booleanRenderer")
    private BooleanRenderer checkboxCellRenderer = new BooleanRenderer();

    @ModelElement("textAreaEditor")
    private TextAreaEditor textAreaEditor = new TextAreaEditor();

    public FormTableForm() {

        person = new Person();
        person.setName("Foo111");

        try {
            domModel = Binder.bind(this, true);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        FormTableForm form = new FormTableForm();

        final JFrame f = new JFrame("Test");
        f.setSize(500, 200);

        f.getContentPane().add(form);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                f.setVisible(true);
            }
        });
    }

    public void onClick() {
        person.setName("name"+new Random().nextInt(80));
        person.setAge(new Random().nextInt(80));
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public class Person {
        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
        private String name;
        private String lastName;
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

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            Object old = this.lastName;
            this.lastName = lastName;
            pcs.firePropertyChange("lastName", old, lastName);
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
