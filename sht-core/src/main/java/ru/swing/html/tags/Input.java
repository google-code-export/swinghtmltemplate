package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import ru.swing.html.DomModel;
import ru.swing.html.Utils;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 20.11.2010
 * Time: 23:58:42
 */
public class Input extends Tag {

    private Log logger = LogFactory.getLog(getClass());
    @Override
    public JComponent createComponent() {
        String type = getType();

        JComponent field;
        if ("text".equals(type) || StringUtils.isEmpty(type)) {
            field = new JTextField();
        }
        else if ("button".equals(type)) {
            field = new JButton();
        }
        else if ("password".equals(type)) {
            field = new JPasswordField();
        }
        else if ("checkbox".equals(type)) {
            field = new JCheckBox();
        }
        else if ("radio".equals(type)) {
            field = new JRadioButton();
        }
        else {
            logger.warn("Unknown field type: '"+type+"', reseting to 'text'");
            field = new JTextField();
        }

        setComponent(field);
        return field;
    }

    @Override
    public void applyAttributes(JComponent component) {
        super.applyAttributes(component);
        if (component instanceof JTextComponent) {
            JTextComponent c = (JTextComponent) getComponent();
            c.setText(getContent());
        }


        if (component instanceof AbstractButton) {
            AbstractButton b = (AbstractButton) component;
            String borderPainted = getAttribute("border-painted");
            String contentAreaFilled = getAttribute("content-area-filled");
            if (StringUtils.isNotEmpty(borderPainted)) {
                b.setBorderPainted((Boolean) Utils.convertStringToObject(borderPainted, Boolean.class));
            }
            if (StringUtils.isNotEmpty(contentAreaFilled)) {
                b.setContentAreaFilled((Boolean) Utils.convertStringToObject(contentAreaFilled, Boolean.class));
            }
        }

        //perform binding
        if (StringUtils.isNotEmpty(getAttribute("value"))) {
            String el = getAttribute("value");
            BeanProperty componentProperty;
            String type = getType();

            if ("text".equals(type) || "password".equals(type) || StringUtils.isEmpty(type)) {
                componentProperty = BeanProperty.create("text");
            }
            else if ("checkbox".equals(type)) {
                componentProperty = BeanProperty.create("selected");
            }
            else {
                componentProperty = BeanProperty.create("text");
            }

            bind(el, getComponent(), componentProperty, AutoBinding.UpdateStrategy.READ_WRITE);
        }


    }
}
