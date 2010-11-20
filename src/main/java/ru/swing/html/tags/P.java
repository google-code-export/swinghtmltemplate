package ru.swing.html.tags;

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
        //todo обертку html делать в зависимости от параметров
        label.setText("<html>"+getContent()+"</html>");
    }

    @Override
    public void handleLayout() {
    }

    @Override
    public void handleChildren() {
    }


}
