package ru.swing.html.tags.swing;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.BeanProperty;
import ru.swing.html.ELUtils;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * <p>Tag is converted to javax.swing.JSpinner</p>
 * <p>Tag supports attributes:</p>
 * <ul>
 *     <li>value - en EL expression, describing a property of a model element, the value of the component is binded to.
 *  </ul>
 *  <h2>Example:</h2>
 *  <pre>
 *   &lt;c:spinner /&gt;
 *  </pre>
 */
public class Spinner extends Tag {


    public static final String VALUE_ATTRIBUTE = "value";
    private Log logger = LogFactory.getLog(getClass());
    @Override
    public JComponent createComponent() {
        JSpinner field = new JSpinner();
        setComponent(field);
        return field;
    }

    @Override
    public void applyAttribute(JComponent component, String name) {
        JSpinner c = (JSpinner) getComponent();

        if (TAG_CONTENT.equals(name)) {
            String content = ELUtils.parseStringValue(getContent(), getModelElements());
            if (StringUtils.isNotEmpty(content)) {
                try {
                    Integer value = new Integer(content);
                    c.setValue(value);
                } catch (NumberFormatException e) {
                    logger.warn(content +" cannot be parsed as integer");
                }
            }
        }
        else if (VALUE_ATTRIBUTE.equals(name)) {
            //perform binding
            if (StringUtils.isNotEmpty(getAttribute(VALUE_ATTRIBUTE))) {
                BeanProperty componentProperty = BeanProperty.create(VALUE_ATTRIBUTE);
                getModel().bind(getAttribute(VALUE_ATTRIBUTE), getComponent(), componentProperty);
            }
        }
        else {
            super.applyAttribute(component, name);
        }

    }

}
