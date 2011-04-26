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
 * Tests &lt;textarea value='${...}'/&gt; binding.
 */
public class TextAreaBinding extends TestCase {

    public void testTextBinding() throws Exception {

        String html =
                "<html>" +
                "<body>" +
                "<textarea id='comp' value='${foo.name}'/>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));

        Foo foo = new Foo();
        model.addModelElement("foo", foo);

        DomConverter.toSwing(model);
        JTextArea f = (JTextArea) model.getTagById("comp").getComponent();

        foo.setName("test");
        assertEquals(foo.getName(), f.getText());

        f.setText("test");
        assertEquals(f.getText(), foo.getName());

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
