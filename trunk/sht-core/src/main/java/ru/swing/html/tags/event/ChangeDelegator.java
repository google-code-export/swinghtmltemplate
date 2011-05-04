package ru.swing.html.tags.event;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.lang.reflect.Method;

/**
 * Delegator for ChangeEvent
 */
public class ChangeDelegator extends BaseDelegator implements ChangeListener {

    public ChangeDelegator(Object controller, Method finalM) {
        super(controller, finalM);
    }

    public void stateChanged(ChangeEvent e) {
        delegate(e);
    }
}
