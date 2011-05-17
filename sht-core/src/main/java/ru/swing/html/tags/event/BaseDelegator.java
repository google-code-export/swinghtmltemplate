package ru.swing.html.tags.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.configuration.MethodInvoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Delegates execution to the specified method and controller.
 */
public class BaseDelegator {

    private final MethodInvoker invoker;
    private Log logger = LogFactory.getLog(getClass());

    public BaseDelegator(MethodInvoker invoker) {
        this.invoker = invoker;
    }

    protected Object delegate(Class argType, Object event) {
        return invoker.invoke(argType, event);
    }

}
