package ru.swing.html.tags.swing;

import ru.swing.html.tags.Tag;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 21.11.2010
 * Time: 0:56:11
 * To change this template use File | Settings | File Templates.
 */
public class Strut extends Tag {

    @Override
    public JComponent createComponent() {
        if ("horizontal".equals(getType())) {
            return (JComponent) Box.createHorizontalStrut(new Integer(getWidth()));
        }
        else {
            return (JComponent) Box.createVerticalStrut(new Integer(getHeight()));
        }
    }

    @Override
    public void handleLayout() {
    }


}
