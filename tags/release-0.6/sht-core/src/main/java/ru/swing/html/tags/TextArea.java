package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.BeanProperty;
import ru.swing.html.DomModel;
import ru.swing.html.ELUtils;
import ru.swing.html.Utils;
import ru.swing.html.tags.Tag;

import javax.swing.*;

/**
 * <p>
 *     Tag is converted to javax.swing.JTextArea. Tag's content is used as component's text.
 * </p>
 * <p>
 *     Tag supports properties:
 *     <ul>
 *         <li>columns - sets the number of columns for this TextArea.</li>
 *         <li>rows - sets the number of rows for this TextArea.</li>
 *         <li>value - en EL expression, describing a property of a model element, the text of the component is binded to.</li>
 *     </ul>
 * </p>
 * <h2>Example</h2>
 * <pre>
 *     &lt;scroll>
 *      &lt;textarea>Some text&lt;/textarea>
 *     &lt;/scroll>
 * </pre>
 */
public class TextArea extends Tag {

    public static final String VALUE_ATTRIBUTE = "value";
    private Log logger = LogFactory.getLog(getClass());
    private String rows;
    private String columns;
    private String value;

    @Override
    public JComponent createComponent() {
        return new JTextArea();
    }

    @Override
    public void applyAttribute(JComponent component, String name) {
        JTextArea textArea = (JTextArea) component;

        if (TAG_CONTENT.equals(name)) {
            textArea.setText(getContent());
        }
        else if (VALUE_ATTRIBUTE.equals(name)) {
            //perform binding
            if (StringUtils.isNotEmpty(getValue())) {
                String el = getValue();
                BeanProperty componentProperty;

                componentProperty = BeanProperty.create("text");
                getModel().bind(el, getComponent(), componentProperty);

            }
        }
        else if ("rows".equals(name)) {
            if (StringUtils.isNotEmpty(getRows())) {
                String rows = ELUtils.parseStringValue(getRows(), getModelElements());
                textArea.setRows(Utils.convertStringToObject(rows, Integer.class));
            }
        }
        else if ("columns".equals(name)) {
            if (StringUtils.isNotEmpty(getColumns())) {
                String columns = ELUtils.parseStringValue(getColumns(), getModelElements());
                textArea.setRows(Utils.convertStringToObject(columns, Integer.class));
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
            setRows(value);
        }
        else if ("columns".equals(name)) {
            setColumns(value);
        }
        else if (VALUE_ATTRIBUTE.equals(name)) {
            setValue(value);
        }
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
