package ru.swing.html.example;

import org.jdom.JDOMException;
import ru.swing.html.Binder;
import ru.swing.html.DomModel;
import ru.swing.html.ModelElement;

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

    public FormTableForm() {

        person = new Person();
        person.setName("Foo");

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

        JFrame f = new JFrame("Test");
        f.setSize(400, 200);

        f.getContentPane().add(form);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
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
            System.out.println("Updated name: "+name);
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
