package ru.swing.html.tags.event;

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

    public MouseListenerClickDelegator(Object controller, Method finalM) {
        this(controller, finalM, 1);
    }
    public MouseListenerClickDelegator(Object controller, Method finalM, int clickCount) {
        super(controller, finalM);
        this.clickCount = clickCount;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount()==clickCount) {
            delegate(e);
        }
    }
}
