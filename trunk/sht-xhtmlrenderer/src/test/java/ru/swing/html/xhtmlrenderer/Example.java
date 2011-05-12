package ru.swing.html.xhtmlrenderer;

import org.jdom.JDOMException;
import ru.swing.html.Binder;
import ru.swing.html.ModelElement;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

public class Example extends JFrame {

    @ModelElement("model")
    private Model model = new Model();

    public static void main(String[] args) throws JDOMException, IOException {
        final Example example = new Example();
        Binder.bind(example, true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                example.setVisible(true);
            }
        });
    }


    public void dump() {
        System.out.println("Text: "+model.getText());
    }

    public class Model {

        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
        private String text = "foo";

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }
    }

}
