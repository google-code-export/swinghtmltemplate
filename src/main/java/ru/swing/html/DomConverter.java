package ru.swing.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 11:03:51
 * </pre>
 */
public class DomConverter {

    private static Log logger = LogFactory.getLog(DomConverter.class);

    public static JComponent toSwing(DomModel model) {

        Tag html = model.getRootTag();
        Tag body = html.getChildByName("body");
        JComponent b = convertComponent(body);
        return b;
    }

    public static JComponent convertComponent(Tag componentTag) {

        JComponent component = componentTag.createComponent();
        componentTag.setComponent(component);
        componentTag.applyAttributes(component);
        if (component!=null) {
            componentTag.handleLayout();
            componentTag.handleChildren();
        }      

        return component;
    }




}
