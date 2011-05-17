package ru.swing.html.configuration;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.BeanProperty;
import ru.swing.html.Utils;
import ru.swing.html.tags.Tag;
import ru.swing.html.tags.event.DocumentDelegator;

import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DefaultMethodResolverService implements MethodResolverService {

    public MethodInvoker resolveMethod(String methodString, Tag tag) {

        int indexOfLastDot = methodString.lastIndexOf('.');

        String methodName;
        Object target;

        if (indexOfLastDot<0) {
            methodName = methodString;
            target = tag.getModel().getController();
        }
        else {
            methodName = methodString.substring(indexOfLastDot+1);

            String el = methodString.substring(0, indexOfLastDot);
            BeanProperty property = BeanProperty.create(el);
            target = property.getValue(tag.getModelElements());
        }

        return new ModelMethodInvoker(target, methodName);


    }


    class ModelMethodInvoker implements MethodInvoker {

        private Object target;
        private String methodName;

        private Log logger = LogFactory.getLog(getClass());

        ModelMethodInvoker(Object target, String methodName) {
            this.target = target;
            this.methodName = methodName;
        }

        public Object invoke(Class argType, Object arg) {
            if (target==null || StringUtils.isEmpty(methodName)) {
                return null;
            }

            //search specified method
            Method method = Utils.findActionMethod(target.getClass(), methodName, argType);

            Object res = null;

            if (method!=null) {
                try {
                    if (method.getParameterTypes().length==0) {
                        res = method.invoke(target);
                    }
                    else {
                        res = method.invoke(target, arg);
                    }
                } catch (IllegalAccessException e) {
                    logger.warn("Can't invoke method "+methodName+" in class "+target.getClass(), e);
                } catch (InvocationTargetException e) {
                    logger.warn("Can't invoke method "+methodName+" in class "+target.getClass(), e);
                }
            }
            else {
                logger.warn("Can't find method "+methodName+" in class "+target.getClass());
            }
            return  res;

        }
    }
}
