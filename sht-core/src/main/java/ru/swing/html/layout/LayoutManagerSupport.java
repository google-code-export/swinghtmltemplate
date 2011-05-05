package ru.swing.html.layout;

import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.awt.*;

/**
 * Helper class to maintain LayoutManager for tag's component.
 */
public interface LayoutManagerSupport {

    /**
     * <p>
     * Implementation must actually add child component to the parent component using
     * specified constraint.
     * </p>
     * <p>
     *     constraint is string, so it must be converted to layout's real constraint. E.g. 'top' must be converted
     *     to BorderLayout.NORTH in the BorderLayoutSupport.
     * </p>
     * @param parent parent component
     * @param child child component to be added to parent
     * @param constraint string constraint
     */
    public void addComponent(JComponent parent, JComponent child, String constraint);

    /**
     * LayoutManager must be created for the tag's component in this method.
     * Implementation can check additional attributes, such as margins and paddings.
     * @param tag tag, layout must be created for it's component
     * @return created LayoutManager
     */
    public LayoutManager createLayout(Tag tag);
}
