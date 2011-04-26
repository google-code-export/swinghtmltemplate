package ru.swing.html.layout;

import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.awt.*;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 11:35:42
 * </pre>
 */
public interface LayoutManagerSupport {

    public void addComponent(JComponent parent, JComponent child, String constraint);
    public LayoutManager createLayout(Tag tag);
}
