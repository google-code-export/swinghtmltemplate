package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.BeanProperty;
import ru.swing.html.DomModel;

import javax.swing.*;

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

        //perform binding
        if (StringUtils.isNotEmpty(getAttribute("value"))) {
            String el = getAttribute("value");
            BeanProperty componentProperty;
            String type = getType();

            componentProperty = BeanProperty.create("text");
            getModel().bind(el, getComponent(), componentProperty);

        }
    }





}
