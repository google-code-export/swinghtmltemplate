package ru.swing.html.tags.event;

import ru.swing.html.configuration.MethodInvoker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

/**
 * Delegates action event to the specified method and controller.
 */
public class ClickDelegator extends BaseDelegator implements ActionListener {


    public ClickDelegator(MethodInvoker invoker) {
        super(invoker);
    }

    public void actionPerformed(ActionEvent e) {
        delegate(ActionEvent.class, e);
    }
}
