package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 20.11.2010
 * Time: 23:51:21
 */
public class Div extends Tag {

    private Log logger = LogFactory.getLog(getClass());

    @Override
    public JComponent createComponent() {
        JPanel c = new JPanel();
        setComponent(c);
        return c;
    }

    @Override
    public void applyAttributes(JComponent component) {
        if (StringUtils.isEmpty(getLayout())) {
            setAttribute("layout", "flow");
        }
        super.applyAttributes(component);
    }


}
