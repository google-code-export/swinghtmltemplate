package ru.swing.html.binding;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;

/**
 * Tests spinner binding.
 */
public class SpinnerBindingTest extends TestCase {

    public void testTextBinding() throws Exception {

        String html =
                "<html xmlns:c='http://www.oracle.com/swing'>" +
                "<body>" +
                "<c:spinner id='comp' value='${foo.value}'/>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));

        Foo foo = new Foo();
        model.addModelElement("foo", foo);

        DomConverter.toSwing(model);
        JSpinner f = (JSpinner) model.getTagById("comp").getComponent();

        foo.setValue(50);
        assertEquals(foo.getValue(), f.getValue());

        f.setValue(100);
        assertEquals(f.getValue(), foo.getValue());

    }


    public class Foo {

        private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
        private Integer value = 0;

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            Integer old = this.value;
            this.value = value;
            propertyChangeSupport.firePropertyChange("value", old, value);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.removePropertyChangeListener(listener);
        }
    }



}
