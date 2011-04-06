package ru.swing.html.tags.event;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.lang.reflect.Method;

/**
 * Delegates document change event to the specified method and controller.
 */
public class DocumentDelegator extends BaseDelegator implements DocumentListener {

    public DocumentDelegator(Object controller, Method finalM) {
        super(controller, finalM);
    }

    public void insertUpdate(DocumentEvent e) {
        delegate(e);
    }

    public void removeUpdate(DocumentEvent e) {
        delegate(e);
    }

    public void changedUpdate(DocumentEvent e) {
        delegate(e);
    }
}
