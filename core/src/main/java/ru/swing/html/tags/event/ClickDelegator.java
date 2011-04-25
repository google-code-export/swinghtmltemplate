package ru.swing.html.tags.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

/**
 * Delegates action event to the specified method and controller.
 */
public class ClickDelegator extends BaseDelegator implements ActionListener {

    public ClickDelegator(Object controller, Method finalM) {
        super(controller, finalM);
    }


    public void actionPerformed(ActionEvent e) {
        delegate(e);
    }
}
