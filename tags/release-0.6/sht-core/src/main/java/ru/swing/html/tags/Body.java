package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.DomModel;

import javax.swing.*;

/**
 * Tag is converted into JPanel with FlowLayout as layout by default.
 */
public class Body extends Tag {

    private Log logger = LogFactory.getLog(getClass());

    @Override
    public JComponent createComponent() {
        JPanel c = new JPanel();
        setComponent(c);
        if (StringUtils.isEmpty(getDisplay())) {
            setAttribute("display", "flow");
        }
        return c;
    }


}
