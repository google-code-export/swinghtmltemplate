package ru.swing.html.builder;

import org.jdesktop.observablecollections.ObservableCollections;
import org.jdom.JDOMException;
import ru.swing.html.*;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 21.04.11
 * Time: 16:58
 */
public class PreviewPanel extends JPanel {

    private DomModel model;

    @ModelElement("tags")
    private TreeModel treeModel;

    @ModelElement("attributes")
    private List<Attribute> attributes = ObservableCollections.observableList(new ArrayList<Attribute>());


    private DomModel document;

    @Bind("contentWrapper")
    private JPanel contentWrapper;


    @ModelElement("tagRenderer")
    private TagTreeRenderer tagTreeRenderer = new TagTreeRenderer();

    public void compose() {
        treeModel = DomModelUtils.toTreeModel(model);
        try {
            document = Binder.bind(this, true);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DomConverter.toSwing(model);
        JComponent root = model.getRootTag().getChildByName("body").getComponent();
        contentWrapper.add(root);

    }


    public void add() {
        Attribute attr = new Attribute();
        attributes.add(attr);
    }

    
    public void apply() {
        JTree tree = (JTree) document.getTagById("tagsTree").getComponent();
        if (tree.getSelectionPath()!=null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            Tag tag = (Tag) node.getUserObject();
            for (Attribute attr : attributes) {
                tag.setAttribute(attr.getName(), attr.getValue());
            }
            tag.applyAttributes(tag.getComponent());
        }
    }



    public void onTagChange(TreeSelectionEvent e) {
        if (e.getNewLeadSelectionPath()!=null) {
            TreePath path = e.getNewLeadSelectionPath();
            attributes.clear();
            if (path.getPathCount()>=1) {



                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                Tag tag = (Tag) node.getUserObject();
                for (String name : tag.getAttributes().keySet()) {
                    Attribute attr = new Attribute();
                    attr.setName(name);
                    attr.setValue(tag.getAttribute(name));
                    attributes.add(attr);
                }
            }
        }
    }


    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public DomModel getModel() {
        return model;
    }

    public void setModel(DomModel model) {
        this.model = model;
    }
}
