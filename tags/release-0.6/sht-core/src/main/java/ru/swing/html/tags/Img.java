package ru.swing.html.tags;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.DomModel;
import ru.swing.html.ELUtils;

import javax.swing.*;

/**
 * Tag converts to JLabel with specified image as label's icon.
 */
public class Img extends Tag {

    private static final String SOURCE_ATTRIBUTE = "src";
    private Log logger = LogFactory.getLog(getClass());
    private String source;

    @Override
    public JComponent createComponent() {
        JLabel label = new JLabel();
        setComponent(label);
        return label;
    }

    @Override
    public void handleLayout() {
    }

    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);
        if (SOURCE_ATTRIBUTE.equals(name)) {
            setSource(ELUtils.parseStringValue(value, getModelElements()));
            setAttribute("icon", getSource());
        }
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
