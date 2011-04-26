package ru.swing.html.tags;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 14.04.11
 * Time: 12:15
 * To change this template use File | Settings | File Templates.
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
    public void applyAttributes(JComponent component) {
        super.applyAttributes(component);
        JSeparator separator = (JSeparator) getComponent();
        String orientation = getType();
        if ("horizontal".equals(orientation)) {
            separator.setOrientation(SwingConstants.HORIZONTAL);
        }
        else {
            separator.setOrientation(SwingConstants.VERTICAL);
        }
    }

    @Override
    public void handleLayout() {
    }

}
