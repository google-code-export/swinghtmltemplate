package ru.swing.html.mydoggy.imagebrowser;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.io.File;

/**
 * Renders File
 */
public class FolderTreeRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        FolderTreeNode node = (FolderTreeNode) value;
        File folder = (File) node.getUserObject();
        label.setText(folder.getName());
        return label;
    }
}
