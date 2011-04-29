package ru.swing.html.tags.ui;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.ELProperty;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.util.Map;

public class If extends Tag {

    private String test;
    private Log logger = LogFactory.getLog(getClass());

    @Override
    public JComponent createComponent() {
        return null;
    }

    @Override
    public void handleLayout() {
    }

    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {

        if (StringUtils.isNotEmpty(getTest())) {
            ELProperty<Object, String> prop = ELProperty.create(getTest());
            Object res = prop.getValue(getModel().getModelElements());
            if (res instanceof Boolean) {
                Boolean show = (Boolean) res;
                if (show) {
                    super.handleChildren(substitutions);
                }
            }
            else {
                logger.warn(toString()+": 'test' resolved to non Boolean value: "+res.getClass().getName());
                super.handleChildren(substitutions);
            }
        }
        else {
            logger.warn(toString()+": 'test' parameter is not set");
            super.handleChildren(substitutions);
        }
    }

    @Override
    public void setAttribute(String name, String value) {
        if ("test".equals(name)) {
            setTest(value);
        }
        else {
            super.setAttribute(name, value );
        }
    }


    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
