package ru.swing.html.configuration;

import org.apache.commons.lang.StringUtils;
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

        ModelMethodInvoker(Object target, String methodName) {
            this.target = target;
            this.methodName = methodName;
        }

        public void invoke(Class argType, Object arg) {
            if (target==null || StringUtils.isEmpty(methodName)) {
                return;
            }

            //search specified method
            Method method = Utils.findActionMethod(target.getClass(), methodName, argType);


            if (method!=null) {
                try {
                    if (method.getParameterTypes().length==0) {
                        method.invoke(target);
                    }
                    else {
                        method.invoke(target, arg);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
