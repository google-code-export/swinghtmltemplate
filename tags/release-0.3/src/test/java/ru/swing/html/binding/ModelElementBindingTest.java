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
 * Tests rebinding of model element.
 */
public class ModelElementBindingTest extends TestCase {

    public void testRebind() throws Exception {
        String html =
                "<html>" +
                "<body>" +
                "<input id='comp' type='text' value='${foo.text}'/>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));

        //create 1st model element
        Foo foo = new Foo();
        foo.setText("test1");
        //make sure listeners list is empty
        assertEquals(0, foo.getListenersCount());

        //add 1st model element to model
        model.addModelElement("foo", foo);

        //perform binding
        DomConverter.toSwing(model);
        JTextField f = (JTextField) model.getTagById("comp").getComponent();

        //make sure listeners are registered on 1st model element
        assertEquals(1, foo.getListenersCount());


        //make sure binding is correct
        assertEquals("test1", f.getText());

        //create 2nd model element
        Foo foo2 = new Foo();
        foo2.setText("test2");
        //make sure listeners list is empty
        assertEquals(0, foo2.getListenersCount());

        //add 2nd model element to model
        model.addModelElement("foo", foo2);

        //perform rebinding
        model.rebindModelElement("foo");

        //make sure listeners are removed from 1st model element
        assertEquals(0, foo.getListenersCount());
        //make sure listeners are registered on 2nd model element
        assertEquals(1, foo2.getListenersCount());
        //make sure new binding is correct
        assertEquals("test2", f.getText());


    }



    public class Foo {

        private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            String old = this.text;
            this.text = text;
            propertyChangeSupport.firePropertyChange("text", old, text);
        }

        public int getListenersCount() {
            return propertyChangeSupport.getPropertyChangeListeners().length;
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.removePropertyChangeListener(listener);
        }
    }

}
