package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 20.11.2010
 * Time: 23:37:43
 */
public class P extends Tag {


    private Log logger = LogFactory.getLog(getClass());
    public static final String CONTENT_ATTRIBUTE = "content";

    @Override
    public JComponent createComponent() {
        JLabel label = new JLabel();
        setComponent(label);
        return label;
    }

    @Override
    public void applyAttributes(JComponent component) {
        super.applyAttributes(component);
        JLabel label = (JLabel) component;
        String contentType = getAttribute(CONTENT_ATTRIBUTE);
        if ("html".equals(contentType)) {
            label.setText("<html>"+getContent()+"</html>");
        }
        else if ("text".equals(contentType)) {
            label.setText(getContent());
        }
        else if (StringUtils.isEmpty(contentType)) {
            label.setText(getContent());
        }
        else {
            logger.warn("Unknown type: "+ contentType +", defaulting to text");
            label.setText(getContent());
        }
    }

    @Override
    public void handleLayout() {
    }

    @Override
    public void handleChildren() {
    }


}
