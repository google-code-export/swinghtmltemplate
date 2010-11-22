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

    @Override
    public JComponent createComponent() {
        JLabel label = new JLabel();
        setComponent(label);
        return label;
    }

    @Override
    public void applyAttributes(JComponent component) {
        setAttribute("icon", getAttribute("src"));
        super.applyAttributes(component);
    }

    @Override
    public void handleLayout() {
    }

    @Override
    public void handleChildren() {
    }
    
}
