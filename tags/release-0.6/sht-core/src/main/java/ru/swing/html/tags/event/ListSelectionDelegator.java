package ru.swing.html.tags.event;

import ru.swing.html.configuration.MethodInvoker;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Delegates list selection event to the specified method and controller.
 */
public class ListSelectionDelegator extends BaseDelegator implements ListSelectionListener {


    public ListSelectionDelegator(MethodInvoker invoker) {
        super(invoker);
    }

    public void valueChanged(ListSelectionEvent e) {
        delegate(ListSelectionEvent.class, e);
    }
}