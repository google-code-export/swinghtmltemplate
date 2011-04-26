package ru.swing.html.tags.swing;

import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.util.Map;

/**
 * Holds EL for JList and JComboBox model.
 */
public class SelectItems extends Tag {

    private String value;

    @Override
    public JComponent createComponent() {
        return null;
    }

    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {
    }

    @Override
    public void handleLayout() {
    }

    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);
        if ("value".equals(name)) {
            setValue(value);
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
