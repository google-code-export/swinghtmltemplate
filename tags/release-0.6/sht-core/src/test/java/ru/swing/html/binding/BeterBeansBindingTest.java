package ru.swing.html.binding;

import junit.framework.TestCase;
import org.jdesktop.beansbinding.*;
import org.junit.Test;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 07.04.11
 * Time: 22:15
 */
public class BeterBeansBindingTest extends TestCase {

    @Test
    public void testBinding() throws Exception {

        Map<String, Object> model = new HashMap<String, Object>();

        Foo foo = new Foo();
        model.put("foo", foo);

        JTextField field = new JTextField();

        ELProperty<Map<String, Object>, String> nameP = ELProperty.create("${foo.name}");
        BeanProperty<JTextField, String> textP = BeanProperty.create("text");

        Binding<Map<String, Object>, String, JTextField, String> binding =
               Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
                       model, nameP, field, textP);

        binding.bind();

        String text = "test";
        foo.setName(text);
        assertEquals(text, field.getText());


        text = "test2";
        field.setText(text);
        assertEquals(text, foo.getName());


    }


    public class Foo {

        private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            String old = this.name;
            this.name = name;
            propertyChangeSupport.firePropertyChange("name", old, name);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.removePropertyChangeListener(listener);
        }
    }
}
