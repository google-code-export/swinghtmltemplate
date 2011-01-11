package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 11.01.11
 * Time: 22:40
 */
public class Label extends Tag {


    private Log logger = LogFactory.getLog(getClass());

    @Override
    public JComponent createComponent() {
        JLabel label = new JLabel();
        setComponent(label);
        return label;
    }

    @Override
    public void applyAttributes(JComponent component) {
        super.applyAttributes(component);
        JLabel label = (JLabel) component;
        label.setText(getContent());
    }

    @Override
    public void handleLayout() {
    }



}
