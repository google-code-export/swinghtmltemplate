package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;

/**
 * Tag is converted into JPanel with BorderLayout as layout by default.
 */
public class Div extends Tag {

    private Log logger = LogFactory.getLog(getClass());

    @Override
    public JComponent createComponent() {
        JPanel c = new JPanel();
        setComponent(c);
        if (StringUtils.isEmpty(getDisplay())) {
            setAttribute("display", "border");
        }
        return c;
    }



}
