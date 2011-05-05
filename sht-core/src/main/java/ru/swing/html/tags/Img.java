package ru.swing.html.tags;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.DomModel;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 20.11.2010
 * Time: 23:47:45
 */
public class Img extends Tag {
    
    private Log logger = LogFactory.getLog(getClass());
    private String source;

    @Override
    public JComponent createComponent() {
        JLabel label = new JLabel();
        setComponent(label);
        return label;
    }

    @Override
    public void applyAttributes(JComponent component) {
        setAttribute("icon", getSource());
        super.applyAttributes(component);
    }

    @Override
    public void handleLayout() {
    }

    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);
        if ("src".equals(name)) {
            setSource(value);
        }
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
