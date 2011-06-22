package ru.swing.html.tags.event;

import ru.swing.html.configuration.MethodInvoker;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.lang.reflect.Method;

/**
 * Delegator for ChangeEvent
 */
public class ChangeDelegator extends BaseDelegator implements ChangeListener {


    public ChangeDelegator(MethodInvoker invoker) {
        super(invoker);
    }

    public void stateChanged(ChangeEvent e) {
        delegate(ChangeEvent.class, e);
    }
}
