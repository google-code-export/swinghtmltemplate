package ru.swing.html.layout;

import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.awt.*;

/**
 * Support for CardLayout.
 */
public class CardLayoutSupport implements LayoutManagerSupport {

    public void addComponent(JComponent parent, JComponent child, Tag childTag, String constraint) {
        parent.add(child, childTag.getId());
    }

    public LayoutManager createLayout(Tag tag) {
        return new CardLayout();
    }
}
