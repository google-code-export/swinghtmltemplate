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
 * Тег предназначен для присвоения значений компоненту родительского тега.
 */
public class Attribute extends Tag {

    private Log logger = LogFactory.getLog(Tag.class);

    public static final String NAME_ATTRIBUTE = "name";
    public static final String VALUE_ATTRIBUTE = "value";

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
    public void applyAttributes(JComponent component) {
        //проставляем атрибут для родительского компонента
        component = getParent().getComponent();
        String name = getAttribute(NAME_ATTRIBUTE);
        String valueStr = getAttribute(VALUE_ATTRIBUTE);
        String type = getAttribute(TYPE_ATTRIBUTE);

        if (StringUtils.isEmpty(type)) {
            type = String.class.getName();
        }


        try {
            Class typeClass = Utils.convertStringToClass(type);

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

}
