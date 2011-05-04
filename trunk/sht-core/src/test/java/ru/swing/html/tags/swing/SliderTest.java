package ru.swing.html.tags.swing;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;

public class SliderTest extends TestCase {

    public void testAttributes() throws Exception {

        Slider slider = new Slider();
        slider.setAttribute("max", "100");
        assertEquals("100", slider.getMax());

        slider.setAttribute("min", "101");
        assertEquals("101", slider.getMin());

        slider.setAttribute("value", "12");
        assertEquals("12", slider.getValue());

        slider.setAttribute("onchange", "foo");
        assertEquals("foo", slider.getOnChange());

    }

    public void testBinding() throws Exception {

        String html = "<html xmlns:j='http://www.oracle.com/swing'>" +
                "<head></head>" +
                "<body>" +
                "   <j:slider id='slider' value='${model.value}' max='1000' min='0'/>" +
                "</body>" +
                "</html>";

        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        Model m = new Model();
        m.setValue(123);
        model.addModelElement("model", m);
        DomConverter.toSwing(model);


        JSlider slider = (JSlider) model.getTagById("slider").getComponent();
        assertEquals(123, slider.getValue());

        slider.setValue(321);
        assertEquals(321, m.getValue());

    }

    public void testPlaceholders() throws Exception {

        String html = "<html xmlns:j='http://www.oracle.com/swing'>" +
                "<head></head>" +
                "<body>" +
                "   <j:slider id='slider' max='${max}' min='${min}'/>" +
                "</body>" +
                "</html>";

        DomModel model = DomLoader.loadModel(new ByteArrayInputStream(html.getBytes()));
        model.addModelElement("max", 123);
        model.addModelElement("min", 111);
        DomConverter.toSwing(model);


        JSlider slider = (JSlider) model.getTagById("slider").getComponent();
        assertEquals(123, slider.getMaximum());
        assertEquals(111, slider.getMinimum());

    }


    public class Model {
        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
        private int value = 100;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            int old = this.value;
            this.value = value;
            pcs.firePropertyChange("value", old, value);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(propertyName, listener);
        }

    }
}
