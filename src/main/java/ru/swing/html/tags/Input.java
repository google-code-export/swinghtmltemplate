package ru.swing.html.tags;

import ru.swing.html.DomModel;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 20.11.2010
 * Time: 23:58:42
 */
public class Input extends Tag {

    @Override
    public JComponent createComponent() {
        String type = getType();

        JComponent field;
        if ("text".equals(type)) {
            field = new JTextField();
        }
        else if ("button".equals(type)) {
            field = new JButton();
        }
        else if ("password".equals(type)) {
            field = new JPasswordField();
        }
        else if ("checkbox".equals(type)) {
            field = new JCheckBox();
        }
        else if ("radio".equals(type)) {
            field = new JRadioButton();
        }
        else {
            field = new JTextField();
        }

        setComponent(field);
        return field;
    }

    @Override
    public void applyAttributes(JComponent component) {
        super.applyAttributes(component);
        if (getComponent() instanceof JTextComponent) {
            JTextComponent c = (JTextComponent) getComponent();
            c.setText(getContent());
        }
    }
}
