package ru.swing.html.tags.event;

import ru.swing.html.configuration.MethodInvoker;

import java.awt.event.MouseEvent;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 26.04.11
 * Time: 14:30
 */
public class MouseListenerClickDelegator extends BaseMouseListenerDelegator {

    private int clickCount;

    public MouseListenerClickDelegator(MethodInvoker invoker) {
        this(invoker, 1);
    }
    public MouseListenerClickDelegator(MethodInvoker invoker, int clickCount) {
        super(invoker);
        this.clickCount = clickCount;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount()==clickCount) {
            delegate(MouseEvent.class, e);
        }
    }
}
