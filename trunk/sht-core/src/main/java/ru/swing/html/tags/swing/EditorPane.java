package ru.swing.html.tags.swing;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.beansbinding.BeanProperty;
import ru.swing.html.tags.Tag;

import javax.swing.*;

public class EditorPane extends Tag {

    private String value;

    @Override
    public JComponent createComponent() {
        JEditorPane c = new JTextPane();
        setComponent(c);
        return c;
    }

    @Override
    public void applyAttributes(JComponent component) {
        super.applyAttributes(component);

        JEditorPane c = (JEditorPane) getComponent();

        if (StringUtils.isNotEmpty(getType())) {
            c.setContentType(getType());
        }
        c.setText(getContent());


        //perform binding
        if (StringUtils.isNotEmpty(getValue())) {
            String el = getValue();
            BeanProperty componentProperty;

            componentProperty = BeanProperty.create("text");
            getModel().bind(el, getComponent(), componentProperty);

        }
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
