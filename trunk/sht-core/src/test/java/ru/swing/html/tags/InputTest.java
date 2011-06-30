package ru.swing.html.tags;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.lang.*;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 24.11.2010
 * Time: 11:21:20
 * </pre>
 */
public class InputTest extends TestCase {

    public void testCreateComponent() throws Exception {

        Input tag = new Input();
        assertEquals(JTextField.class, DomConverter.convertComponent(tag).getClass());

        tag = new Input();
        tag.setAttribute("type", "text");
        assertEquals(JTextField.class, DomConverter.convertComponent(tag).getClass());

        tag = new Input();
        tag.setAttribute("type", "password");
        assertEquals(JPasswordField.class, DomConverter.convertComponent(tag).getClass());

        tag = new Input();
        tag.setAttribute("type", "button");
        assertEquals(JButton.class, DomConverter.convertComponent(tag).getClass());

        tag = new Input();
        tag.setAttribute("type", "checkbox");
        assertEquals(JCheckBox.class, DomConverter.convertComponent(tag).getClass());

        tag = new Input();
        tag.setAttribute("type", "radio");
        assertEquals(JRadioButton.class, DomConverter.convertComponent(tag).getClass());

    }


    public void testSetContent() throws Exception {

        String html = "<html>" +
                "<head></head>" +
                "<body style='display: border;'>" +
                "   <input>Foo</input>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        JComponent root = model.getRootTag().getChildByName("body").getComponent();


        assertEquals(1, root.getComponentCount());

        assertEquals(BorderLayout.class, root.getLayout().getClass());
        BorderLayout l = (BorderLayout) root.getLayout();

        assertTrue(l.getLayoutComponent(root, BorderLayout.CENTER) instanceof JTextField);
        JTextField label = (JTextField) l.getLayoutComponent(root, BorderLayout.CENTER);
        assertEquals("Foo", label.getText());


    }

    public void testSetEmptyValue() throws Exception {

        //check that 'value' attribute overrides tag content
        String html = "<html>" +
                "<head></head>" +
                "<body style='display: border;'>" +
                "   <input value=''>Foo</input>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        JComponent root = model.getRootTag().getChildByName("body").getComponent();


        assertEquals(1, root.getComponentCount());

        assertEquals(BorderLayout.class, root.getLayout().getClass());
        BorderLayout l = (BorderLayout) root.getLayout();

        assertTrue(l.getLayoutComponent(root, BorderLayout.CENTER) instanceof JTextField);
        JTextField textField = (JTextField) l.getLayoutComponent(root, BorderLayout.CENTER);
        assertEquals("", textField.getText());


    }

    public void testSizeAttribute() throws Exception {

        String html = "<html>" +
                "<head></head>" +
                "<body style='display: border;'>" +
                "   <input type='text' size='10'>Foo</input>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);
        JComponent root = model.getRootTag().getChildByName("body").getComponent();


        assertEquals(1, root.getComponentCount());

        assertEquals(BorderLayout.class, root.getLayout().getClass());
        BorderLayout l = (BorderLayout) root.getLayout();

        assertTrue(l.getLayoutComponent(root, BorderLayout.CENTER) instanceof JTextField);
        JTextField label = (JTextField) l.getLayoutComponent(root, BorderLayout.CENTER);
        assertEquals("Foo", label.getText());
        assertEquals(10, label.getColumns());


    }


    public void testBinding() throws Exception {

        String html = "<html>" +
                "<head></head>" +
                "<body style='display: border;'>" +
                "   <input id='in' value='${person.name}'></input>" +
                "</body>" +
                "</html>";
        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        Person element = new Person("1");
        model.addModelElement("person", element);
        DomConverter.toSwing(model);

        JTextField label = (JTextField) model.getTagById("in").getComponent();
        assertEquals("1", label.getText());

        element.setName("2");
        assertEquals("2", label.getText());

        label.setText("3");
        assertEquals("3", element.getName());

        element = new Person("1");
        model.addModelElement("person", element);
        model.rebindModelElement("person");
        assertEquals("1", element.getName());

    }

    public void testButtonAction() throws Exception {
        //test action is assigned to button
        String html = "<html>" +
                "<head xmlns:j=\"http://www.oracle.com/swing\">" +
                "   <j:action actionname='foo' title='foo'/>" +
                "</head>" +
                "<body style='display: border;'>" +
                "   <input id='in' action='foo' type='button' value='${person.name}'></input>" +
                "</body>" +
                "</html>";

        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        DomConverter.toSwing(model);

        JButton b = (JButton) model.getTagById("in").getComponent();
        assertEquals("foo", b.getText());

    }

    public void testUnknownType() {
        Input p = new Input();
        p.setType("foo");
        JComponent jComponent = DomConverter.convertComponent(p);
        assertEquals("Unknown type must be resolved to "+JTextField.class, JTextField.class, jComponent.getClass());
    }


    public class Person {

        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

        private String name;

        public Person() {
        }

        public Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            java.lang.Object old = this.name;
            this.name = name;
            pcs.firePropertyChange("name", old, name);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }
    }

}
