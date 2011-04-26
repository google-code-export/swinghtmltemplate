package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;

/**
 * Тег преобразуется в панель JPanel. По умолчанию подставляется FlowLayout.
 */
public class Body extends Tag {

    private Log logger = LogFactory.getLog(getClass());

    @Override
    public JComponent createComponent() {
        JPanel c = new JPanel();
        setComponent(c);
        return c;
    }

    @Override
    public void applyAttributes(JComponent component) {
        if (StringUtils.isEmpty(getDisplay())) {
            setAttribute("display", "flow");
        }
        super.applyAttributes(component);
    }

}
