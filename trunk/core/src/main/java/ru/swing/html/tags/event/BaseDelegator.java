package ru.swing.html.tags.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Delegates execution to the specified method and controller.
 */
public class BaseDelegator {

    private final Method finalM;
    private final java.lang.Object controller;
    private Log logger = LogFactory.getLog(getClass());

    public BaseDelegator(Object controller, Method finalM) {
        this.finalM = finalM;
        this.controller = controller;
    }

    protected void delegate(Object event) {
        try {
            if (finalM.getParameterTypes().length==0) {
                finalM.invoke(controller);
            }
            else {
                java.lang.Object[] finalP = new java.lang.Object[1];
                finalP[0] = event;
                finalM.invoke(controller, finalP);
            }
        } catch (IllegalAccessException e1) {
            logger.warn("Can't invoke method " + finalM.getName()+" in class "+controller.getClass().getName());
        } catch (InvocationTargetException e1) {
            logger.warn("Can't invoke method " + finalM.getName()+" in class "+controller.getClass().getName());
        }
    }

}
