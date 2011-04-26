package ru.swing.html.builder;

import ru.swing.html.DomModel;
import ru.swing.html.tags.Tag;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

/**
 * Utils to convert DomModel to TreeModel.
 */
public class DomModelUtils {

    /**
     * Converts DomModel to TreeModel. All nodes are DefaultMutableTreeNode, user object is Tag.
     * @param domModel domModel
     * @return treeModel
     */
    public static TreeModel toTreeModel(DomModel domModel) {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(domModel.getRootTag());
        load(root, domModel.getRootTag());
        DefaultTreeModel model = new DefaultTreeModel(root);
        return model;

    }


    /**
     * Recursively creates sub-nodes for tag's children and adds them to node.
     * New sub-nodes are DefaultMutableTreeNode with Tag as user object.
     * @param node parent node, new sub-nodes will be added to it
     * @param tag parent tag. new sub-nodes will be created from the children oh this tag.
     */
    public static void load(DefaultMutableTreeNode node, Tag tag) {
        for (Tag child : tag.getChildren()) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
            node.add(childNode);
            load(childNode, child);
        }
    }


}
