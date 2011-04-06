package ru.swing.html.tags;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.DomModel;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 20.11.2010
 * Time: 23:50:01
 */
public class TextArea extends Tag {

    private Log logger = LogFactory.getLog(getClass());

    @Override
    public JComponent createComponent() {
        JTextArea c = new JTextArea();
        setComponent(c);
        return c;
    }

    @Override
    public void applyAttributes(JComponent component) {
        super.applyAttributes(component);
        JTextArea textArea = (JTextArea) component;
        textArea.setText(getContent());
    }

    @Override
    public void handleLayout() {
    }


}
