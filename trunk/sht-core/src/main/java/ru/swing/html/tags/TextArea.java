package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.BeanProperty;
import ru.swing.html.DomModel;
import ru.swing.html.Utils;
import ru.swing.html.tags.Tag;

import javax.swing.*;

public class TextArea extends Tag {

    private Log logger = LogFactory.getLog(getClass());
    private int rows = -1;
    private int columns = -1;
    private String value;

    @Override
    public JComponent createComponent() {
        JTextArea c = new JTextArea();
        setComponent(c);
        return c;
    }

    @Override
    public void applyAttribute(JComponent component, String name) {
        JTextArea textArea = (JTextArea) component;

        if (TAG_CONTENT.equals(name)) {
            textArea.setText(getContent());
        }
        else if ("value".equals(name)) {
            //perform binding
            if (StringUtils.isNotEmpty(getValue())) {
                String el = getValue();
                BeanProperty componentProperty;

                componentProperty = BeanProperty.create("text");
                getModel().bind(el, getComponent(), componentProperty);

            }
        }
        else if ("rows".equals(name)) {
            if (getRows()>=0) {
                textArea.setRows(getRows());
            }
        }
        else if ("columns".equals(name)) {
            if (getColumns()>=0) {
                textArea.setColumns(getColumns());
            }
        }
        else {
            super.applyAttribute(component, name);
        }


    }

    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);

        if ("rows".equals(name)) {
            setRows(Utils.convertStringToObject(value, Integer.class));
        }
        else if ("columns".equals(name)) {
            setColumns(Utils.convertStringToObject(value, Integer.class));
        }
        else if ("value".equals(name)) {
            setValue(value);
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
