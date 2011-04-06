package ru.swing.html.tags;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 21.11.2010
 * Time: 0:44:35
 */
public class Glue extends Tag {

    @Override
    public JComponent createComponent() {
        if ("horizontal".equals(getType())) {
            return (JComponent) Box.createHorizontalGlue();
        }
        else {
            return (JComponent) Box.createVerticalGlue();
        }
    }

    @Override
    public void handleLayout() {
    }

}
