package ru.swing.html.tags.swing;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.beansbinding.BeanProperty;
import ru.swing.html.ELUtils;
import ru.swing.html.tags.Tag;

import javax.swing.*;

/**
 * <p>Tag is converted to the `javax.swing.JEditorPane`. The content type for the document is set with `type` attribute:</p>
 * <pre>
 * &lt;c:editorPane value="${model.selectedProject.description}" type='text/html' readonly='true' >
 *     &lt;c:attribute name="preferredSize" value="0 80" type='java.awt.Dimension'/>
 * &lt;/c:editorPane>
 * </pre>
 *
 * <p>The `value` attibute, if presents, sets the binding for the component. If it is absent, then usual text component
 * attributes are applied: the content of the tag is set as component text.</p>
 *
 */
public class EditorPane extends Tag {

    private String value;

    @Override
    public JComponent createComponent() {
        return new JTextPane();
    }

    @Override
    public void applyAttribute(JComponent component, String name) {

        JEditorPane c = (JEditorPane) getComponent();

        if (TYPE_ATTRIBUTE.equals(name)) {
            if (StringUtils.isNotEmpty(getType())) {
                c.setContentType(ELUtils.parseStringValue(getType(), getModelElements()));
            }
        }
        else if (TAG_CONTENT.equals(name)) {
            c.setText(ELUtils.parseStringValue(getContent(), getModelElements()));
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
        else {
            super.applyAttribute(component, name);
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
