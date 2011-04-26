package ru.swing.html.builder;

import ru.swing.html.DomModel;
import ru.swing.html.tags.Tag;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 21.04.11
 * Time: 16:42
 */
public class DomModelUtils {

    public static TreeModel toTreeModel(DomModel domModel) {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(domModel.getRootTag());
        load(root, domModel.getRootTag());
        DefaultTreeModel model = new DefaultTreeModel(root);
        return model;

    }


    public static void load(DefaultMutableTreeNode node, Tag tag) {
        for (Tag child : tag.getChildren()) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
            node.add(childNode);
            load(childNode, child);
        }
    }


}
