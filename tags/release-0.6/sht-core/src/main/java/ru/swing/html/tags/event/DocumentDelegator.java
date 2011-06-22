package ru.swing.html.tags.event;

import ru.swing.html.configuration.MethodInvoker;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.lang.reflect.Method;

/**
 * Delegates document change event to the specified method and controller.
 */
public class DocumentDelegator extends BaseDelegator implements DocumentListener {


    public DocumentDelegator(MethodInvoker invoker) {
        super(invoker);
    }

    public void insertUpdate(DocumentEvent e) {
        delegate(DocumentEvent.class, e);
    }

    public void removeUpdate(DocumentEvent e) {
        delegate(DocumentEvent.class, e);
    }

    public void changedUpdate(DocumentEvent e) {
        delegate(DocumentEvent.class, e);
    }
}
