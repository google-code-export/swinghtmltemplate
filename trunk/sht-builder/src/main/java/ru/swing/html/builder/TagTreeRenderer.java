package ru.swing.html.builder;

import org.apache.commons.lang.StringUtils;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class TagTreeRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Tag tag = (Tag) node.getUserObject();

        StringBuilder sb = new StringBuilder(tag.getName());
        if (StringUtils.isNotEmpty(tag.getId())) {
            sb.append("#").append(tag.getId());
        }

        JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        label.setText(sb.toString());
        return label;
    }
}
