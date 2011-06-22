package ru.swing.html.tags.event;

import ru.swing.html.configuration.MethodInvoker;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 26.04.11
 * Time: 14:28
 */
public abstract class BaseMouseListenerDelegator extends BaseDelegator implements MouseListener {


    public BaseMouseListenerDelegator(MethodInvoker invoker) {
        super(invoker);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
