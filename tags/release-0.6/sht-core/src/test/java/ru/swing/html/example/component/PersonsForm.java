package ru.swing.html.example.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;
import ru.swing.html.Binder;
import ru.swing.html.DomModel;
import ru.swing.html.ModelElement;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test &lt;component> tag
 */
public class PersonsForm extends JFrame {

    @ModelElement("model")
    private Model model = new Model();

    private DomModel domModel;

    private Log logger = LogFactory.getLog(getClass());

    public PersonsForm() throws JDOMException, IOException {
        model.getPersons().add(new Person("Bill", "1-123-00"));
        model.getPersons().add(new Person("Mary", "1-123-01"));
        model.getPersons().add(new Person("Mike", "1-123-02"));
        model.getPersons().add(new Person("Nina", "1-123-03"));
        model.getPersons().add(new Person("Jane", "1-123-04"));
        DomModel model = Binder.bind(this, true);
        this.domModel = model;
    }


    public static void main(String[] args) throws JDOMException, IOException {
        final PersonsForm personsForm = new PersonsForm();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                personsForm.setVisible(true);
            }
        });
    }

    public void dump() {
        for (Person person : model.getPersons()) {
            logger.info(person.getName()+" - "+person.getPhone());
        }
        logger.info(domModel.dump());
    }


    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public class Model {
        private List<Person> persons = new ArrayList<Person>();

        public List<Person> getPersons() {
            return persons;
        }

        public void setPersons(List<Person> persons) {
            this.persons = persons;
        }
    }

    public class Person {

        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
        private String name;
        private String phone;

        private Person() {
        }

        private Person(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            String old = this.name;
            this.name = name;
            pcs.firePropertyChange("name", old, name);
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            String old = this.phone;
            this.phone = phone;
            pcs.firePropertyChange("phone", old, phone);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(propertyName, listener);
        }
    }

}
