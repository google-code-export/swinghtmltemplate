package ru.swing.html.tags.swing;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.Utils;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * <p>Special tag for assigning value to the parent's tag component. Doesn't convert to any swing component.</p>
 *
 * <p>Supported attributes for tag:</p>
 * <ul>
 *  <li>name - field name, to which the value will be assigned. Example: `preferredSize`
 *  <li>value - value to be assigned, as string
 *  <li>type - field type. Full class name. Example, `java.awt.Dimension`
 * </ul>
 *
 * <p>Supported `value` formats according to `type` value:</p>
 * <ul>
 *  <li>type='java.lang.String'` : string, example: `&lt;attribute name='name' value='Foo' type='java.lang.String'/>`
 *  <li>type='boolean'` : `true` or `false`, example: `&lt;attribute name='autoscrolls' value='false' type='boolean'/>`
 *  <li>type='int'` : integer, example: `&lt;attribute name='width' value='500' type='int'/>`
 *  <li>type='long'` : long, example: `&lt;attribute name='uuid' value='50000' type='long'/>`
 *  <li>type='float'` : float, example: `&lt;attribute name='width' value='5.0' type='float'/>`
 *  <li>type='double'` : double, example: `&lt;attribute name='width' value='5.0' type='double'/>`
 *  <li>type='short'` : short, example: `&lt;attribute name='width' value='5' type='short'/>`
 *  <li>type='byte'` : byte, example: `&lt;attribute name='width' value='5' type='byte'/>`
 *  <li>type='char'` : charachter, example: `&lt;attribute name='mnemonic' value='f' type='char'/>`
 *  <li>type='java.lang.Boolean'` : `true` or `false`, example: `&lt;attribute name='autoscrolls' value='false' type='java.lang.Boolean'/>`
 *  <li>type='java.lang.Integer'` : integer, example: `&lt;attribute name='width' value='500' type='java.lang.Integer'/>`
 *  <li>type='java.lang.Long'` : long, example: `&lt;attribute name='uuid' value='50000' type='java.lang.Long'/>`
 *  <li>type='java.lang.Float'` : float, example: `&lt;attribute name='width' value='5.0' type='java.lang.Float'/>`
 *  <li>type='java.lang.Double'` : double, example: `&lt;attribute name='width' value='5.0' type='java.lang.Double'/>`
 *  <li>type='java.lang.Byte'` : byte, example: `&lt;attribute name='width' value='5' type='java.lang.Byte'/>`
 *  <li>type='java.lang.Short'` : short, example: `&lt;attribute name='width' value='5' type='java.lang.Short'/>`
 *  <li>type='java.lang.Charachter'` : charachter, example: `&lt;attribute name='mnemonic' value='f' type='java.lang.Charachter'/>`
 *  <li>type='java.awt.Dimension'` : 2 integers, separated by spaces, example: `&lt;attribute name='preferredSize' value='500 100' type='java.awt.Dimension'/>`
 *  <li>type='java.awt.Insets'` : 4 integers, separated by spaces, example: `&lt;attribute name='insets' value='5 1 3 4' type='java.awt.Insets'/>`
 *  <li>type='java.awt.Point'` : 2 integers, separated by spaces, example: `&lt;attribute name='location' value='5 1' type='java.awt.Point'/>`
 *  <li>type='java.awt.Rectangle'` : 4 integers, separated by spaces, example: `&lt;attribute name='bounds' value='5 1 3 4' type='java.awt.Rectangle'/>`
 * </ul>
 * <p>Pay attention, that methods with primitive types as operands are not equal to methods with objects as operands.
 * `setFoo(java.lang.Integer)` is not equal to `setFoo(int)`. You must specify correct `type` value.</p>
 *
 * <h2>Example:</h2>
 * <pre>
 * &lt;html xmlns:c="http://www.oracle.com/swing">
 * &lt;body>
 *   &lt;c:attribute name='preferredSize' value='500 100' type='java.awt.Dimension'/>
 *   &lt;p>Panel has preferred size 500x100px&lt;/p>
 * &lt;/body>
 * &lt;/html>
 * </pre>
 *
 */
public class Attribute extends Tag {

    private Log logger = LogFactory.getLog(Tag.class);

    public static final String NAME_ATTRIBUTE = "name";
    public static final String VALUE_ATTRIBUTE = "value";

    private String attributeName;
    private String value;

    @Override
    public JComponent createComponent() {
        return null;
    }

    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {
    }

    @Override
    public void handleLayout() {
    }

    @Override
    public void applyAttribute(JComponent component, String attrName) {
        if (VALUE_ATTRIBUTE.equals(attrName)) {
            //attribute will be applied to the parent's tag component
            component = getParent().getComponent();

            String name = getAttributeName();
            String valueStr = getValue();
            String type = getType();

            if (StringUtils.isEmpty(type)) {
                type = String.class.getName();
            }


            try {
                Class<java.lang.Object> typeClass = Utils.convertStringToClass(type);

                java.lang.Object value = Utils.convertStringToObject(valueStr, typeClass);

                String setterName = "set"+StringUtils.capitalize(name);
                Method m = component.getClass().getMethod(setterName, typeClass);
                m.invoke(component, value);
                logger.trace("Assigned value "+value+" ["+valueStr+"] to component ["+component+"] of tag "+getParent().getName());

            } catch (ClassNotFoundException e) {
                logger.error("Can't find class for type: "+type, e);
            } catch (NoSuchMethodException e) {
                logger.error("Can't find setter for property "+name, e);
            } catch (IllegalAccessException e) {
                logger.warn("Can't assign value '"+valueStr+"' to field '"+name+"': "+e.getMessage(), e);
            } catch (InvocationTargetException e) {
                logger.warn("Can't assign value '"+valueStr+"' to field '"+name+"': "+e.getMessage(), e);
            }

        }
        else {
            super.applyAttribute(component, attrName);
        }
    }


    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);

        if (NAME_ATTRIBUTE.equals(name)) {
            setAttributeName(value);
        }
        else if (VALUE_ATTRIBUTE.equals(name)) {
            setValue(value);
        }

    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
