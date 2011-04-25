package ru.swing.html.tags.swing;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.BeanProperty;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * Corresponds to JSpinner.
 */
public class Spinner extends Tag {


    private Log logger = LogFactory.getLog(getClass());
    @Override
    public JComponent createComponent() {
        JSpinner field = new JSpinner();
        setComponent(field);
        return field;
    }

    @Override
    public void applyAttributes(JComponent component) {
        super.applyAttributes(component);
        JSpinner c = (JSpinner) getComponent();
        if (StringUtils.isNotEmpty(getContent())) {
            try {
                Integer value = new Integer(getContent());
                c.setValue(value);
            } catch (NumberFormatException e) {
                logger.warn(getContent()+" cannot be parsed as integer");
            }
        }

        //perform binding
        if (StringUtils.isNotEmpty(getAttribute("value"))) {
            BeanProperty componentProperty = BeanProperty.create("value");
            getModel().bind(getAttribute("value"), getComponent(), componentProperty);
        }


    }


}
