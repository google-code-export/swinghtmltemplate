package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.ELUtils;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 11.01.11
 * Time: 22:40
 */
public class Label extends Tag {


    private Log logger = LogFactory.getLog(getClass());

    @Override
    public JComponent createComponent() {
        JLabel label = new JLabel();
        setComponent(label);
        return label;
    }

    @Override
    public void applyAttribute(JComponent component, String name) {
        if (TAG_CONTENT.equals(name)) {
            JLabel label = (JLabel) component;
            label.setText(ELUtils.parseStringValue(getContent(), getModelElements()));
        }
        else {
            super.applyAttribute(component, name);
        }
    }

    @Override
    public void handleLayout() {
    }

    @Override
    public void afterComponentsConverted() {
        super.afterComponentsConverted();
        String targetId = getAttribute("for");
        if (StringUtils.isNotEmpty(targetId)) {
            Tag targetTag = getModel().getTagById(targetId);
            if (targetTag!=null && targetTag.getComponent()!=null) {
                JLabel label = (JLabel) getComponent();
                label.setLabelFor(targetTag.getComponent());
            }
            else {
                logger.warn("Can't find target with id '"+targetId+"' for label");
            }
        }
    }
}
