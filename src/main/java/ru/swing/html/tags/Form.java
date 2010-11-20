package ru.swing.html.tags;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 21.11.2010
 * Time: 0:02:18
 * To change this template use File | Settings | File Templates.
 */
public class Form extends Tag {

    private Log logger = LogFactory.getLog(getClass());

    @Override
    public JComponent createComponent() {
        JPanel c = new JPanel();
        setComponent(c);
        return c;
    }

    @Override
    public void applyAttributes(JComponent component) {
        setLayout("mig");
        super.applyAttributes(component);
    }


}
