package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.BeanProperty;
import ru.swing.html.DomModel;
import ru.swing.html.Utils;

import javax.swing.*;

public class TextArea extends Tag {

    private Log logger = LogFactory.getLog(getClass());
    private int rows = -1;
    private int columns = -1;

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

        if (getRows()>=0) {
            textArea.setRows(getRows());
        }
        if (getColumns()>=0) {
            textArea.setColumns(getColumns());
        }
    }

    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);

        if ("rows".equals(name)) {
            setRows((Integer) Utils.convertStringToObject(value, Integer.class));
        }
        else if ("columns".equals(name)) {
            setColumns((Integer) Utils.convertStringToObject(value, Integer.class));
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
}
