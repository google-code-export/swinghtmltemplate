package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.beansbinding.BeanProperty;

import javax.swing.*;

public class EditorPane extends Tag  {

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
        if (StringUtils.isNotEmpty(getAttribute("value"))) {
            String el = getAttribute("value");
            BeanProperty componentProperty;

            componentProperty = BeanProperty.create("text");
            getModel().bind(el, getComponent(), componentProperty);

        }
    }
}
