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
According to `type` value converts to
 <ul>
  <li>type="text" - `JTextField`</li>
  <li>type="password" - `JPasswordField`</li>
  <li>type="button" - `JButton`</li>
  <li>type="checkbox" - `JCheckBox`</li>
  <li>type="radio" - `JRadioButton`</li>
 </ul>
 <p>Converts to `JTextField` by default. If `type` equals to unknown string, `type` will be resolved to
`text`, warning will be printed to log.</p>

 <pre>
 &lt;input type="button" text="OK"/>
 </pre>

 <p>
 If resulting component is subclass of `javax.swing.text.JTextComponent`, then the contents of the tag will be component's text.
 </p>
 <pre>
 &lt;input type="text">Initial text&lt;/input>
 </pre>

 <p>&lt;input>` with type `text`, `password` and `checkbox` can be binded to the model element property using
value attribute:</p>
 <pre>
 &lt;input type='text' value='${account.login}'/>
 </pre>
 <p>text inputs can be binded to `java.lang.String` type properties.
 checkboxes can be binded to `boolean` (not `java.lang.Boolean`) type properties.</p>
 */
public class Input extends Tag {

    public static final String VALUE_ATTRIBUTE = "value";
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
    public void applyAttribute(JComponent component, String name) {

        //tag content is always equals to empty string, so when binding is uses,
        //an empty string will be assigned to binded model property during conversion phase.
        //to avoid this check that no binding is used
        if (TAG_CONTENT.equals(name) && StringUtils.isEmpty(getAttribute(VALUE_ATTRIBUTE))) {
            if (component instanceof JTextComponent) {
                JTextComponent c = (JTextComponent) getComponent();
                c.setText(getContent());
            }
        }
        else if ("border-painted".equals(name)) {
            if (component instanceof AbstractButton) {
                AbstractButton b = (AbstractButton) component;
                String borderPainted = getAttribute("border-painted");
                if (StringUtils.isNotEmpty(borderPainted)) {
                    b.setBorderPainted(Utils.convertStringToObject(borderPainted, Boolean.class));
                }
            }
        }
        else if ("content-area-filled".equals(name)) {
            if (component instanceof AbstractButton) {
                AbstractButton b = (AbstractButton) component;
                String contentAreaFilled = getAttribute("content-area-filled");
                if (StringUtils.isNotEmpty(contentAreaFilled)) {
                    b.setContentAreaFilled(Utils.convertStringToObject(contentAreaFilled, Boolean.class));
                }
            }
        }
        else if (VALUE_ATTRIBUTE.equals(name)) {
            //perform binding
            if (StringUtils.isNotEmpty(getAttribute(VALUE_ATTRIBUTE))) {
                String el = getAttribute(VALUE_ATTRIBUTE);
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
        else {
            super.applyAttribute(component, name);
        }

    }
}
