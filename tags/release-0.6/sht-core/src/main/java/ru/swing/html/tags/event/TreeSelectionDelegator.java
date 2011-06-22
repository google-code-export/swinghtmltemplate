package ru.swing.html.tags.event;

import ru.swing.html.configuration.MethodInvoker;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

/**
 * Delegates action event to the specified method and controller.
 */
public class TreeSelectionDelegator extends BaseDelegator implements TreeSelectionListener {


    public TreeSelectionDelegator(MethodInvoker invoker) {
        super(invoker);
    }

    public void valueChanged(TreeSelectionEvent e) {
        delegate(TreeSelectionEvent.class, e);
    }
}
