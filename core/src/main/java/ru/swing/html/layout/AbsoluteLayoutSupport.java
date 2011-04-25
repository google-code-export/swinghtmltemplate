package ru.swing.html.layout;

import org.apache.commons.lang.StringUtils;
import ru.swing.html.Utils;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.awt.*;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 26.11.2010
 * Time: 10:30:10
 * </pre>
 */
public class AbsoluteLayoutSupport implements LayoutManagerSupport {

    public void addComponent(JComponent parent, JComponent child, String constraint) {
        if (!StringUtils.isEmpty(constraint)) {
            Rectangle bounds = (Rectangle) Utils.convertStringToObject(constraint, Rectangle.class);
            child.setBounds(bounds);
        }
        parent.add(child);
    }

    public LayoutManager createLayout(Tag tag) {
        return null;
    }
}
