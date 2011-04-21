package ru.swing.html.tags;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;

/**
 * Тег преобразуется в панель JPanel с miglayout в качестве менеджера компоновки.
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
        setDisplay("mig");
        super.applyAttributes(component);
    }


}
