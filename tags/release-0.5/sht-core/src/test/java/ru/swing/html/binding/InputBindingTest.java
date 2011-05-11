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
 * Test &lt;input type='...' value='${...}'/&gt; binding
 */
public class InputBindingTest extends TestCase {

    public void testTextBinding() throws Exception {

        String html =
                "<html>" +
                "<body>" +
                "<input id='comp' type='text' value='${foo.text}'/>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));

        Foo foo = new Foo();
        model.addModelElement("foo", foo);

        DomConverter.toSwing(model);
        JTextField f = (JTextField) model.getTagById("comp").getComponent();

        foo.setText("test");
        assertEquals(foo.getText(), f.getText());

        f.setText("test");
        assertEquals(f.getText(), foo.getText());

    }
    
    public void testBooleanBinding() throws Exception {

        String html =
                "<html>" +
                "<body>" +
                "<input id='comp' type='checkbox' value='${foo.bool}'/>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));

        Foo foo = new Foo();
        model.addModelElement("foo", foo);

        DomConverter.toSwing(model);
        JCheckBox f = (JCheckBox) model.getTagById("comp").getComponent();

        foo.setBool(true);
        assertEquals(foo.getBool(), f.isSelected());

        foo.setBool(false);
        assertEquals(foo.getBool(), f.isSelected());

        f.setSelected(true);
        assertEquals(f.isSelected(), foo.getBool());

        f.setSelected(false);
        assertEquals(f.isSelected(), foo.getBool());

    }






    public class Foo {

        private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
        private String text;
        private boolean bool;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            String old = this.text;
            this.text = text;
            propertyChangeSupport.firePropertyChange("text", old, text);
        }

        public boolean getBool() {
            return bool;
        }

        public void setBool(boolean bool) {
            Boolean old = this.bool;
            this.bool = bool;
            propertyChangeSupport.firePropertyChange("bool", Boolean.valueOf(old), Boolean.valueOf(bool));
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.removePropertyChangeListener(listener);
        }
    }
}
