package ru.swing.html.tags;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;

/**
 * Tag is converted to JSeparator.
 */
public class Hr extends Tag  {

    private Log logger = LogFactory.getLog(getClass());

    @Override
    public JComponent createComponent() {
        JSeparator separator = new JSeparator();
        setComponent(separator);
        return separator;
    }

    @Override
    public void applyAttribute(JComponent component, String name) {
        JSeparator separator = (JSeparator) getComponent();
        if (TYPE_ATTRIBUTE.equals(name)) {
            String orientation = getType();
            if ("horizontal".equals(orientation)) {
                separator.setOrientation(SwingConstants.HORIZONTAL);
            }
            else {
                separator.setOrientation(SwingConstants.VERTICAL);
            }
        }
        else {
            super.applyAttribute(component, name);
        }
    }

    @Override
    public void handleLayout() {
    }

}
