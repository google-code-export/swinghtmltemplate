package examples.xhtmlrenderer;


import java.awt.Dimension
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import javax.swing.JFrame
import javax.swing.JTextField
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreeModel
import org.xhtmlrenderer.simple.XHTMLPanel
import ru.swing.html.Bind
import ru.swing.html.ModelElement

public class Example extends JFrame {

    @ModelElement("model")
    private Model model = new Model();

    @Bind("text")
    private JTextField text;

    @Bind("xhtml")
    private XHTMLPanel xhtml;


    public void dump() {
        System.out.println("Text: "+model.getText());
    }

    public void resize() {
        text.setPreferredSize(new Dimension(250, 30));
        xhtml.relayout();

    }

    public class Model {

        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
        private String text = "foo";
        private TreeModel treeModel;

        private java.util.List<Integer> items = Arrays.asList(1, 2, 3);


        public Model() {
            DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
            treeModel = new DefaultTreeModel(root);
            DefaultMutableTreeNode child1 = new DefaultMutableTreeNode("Child 1");
            child1.add(new DefaultMutableTreeNode("Child 11"));
            child1.add(new DefaultMutableTreeNode("Child 12"));
            child1.add(new DefaultMutableTreeNode("Child 13"));
            child1.add(new DefaultMutableTreeNode("Child 14"));
            root.add(child1);
            DefaultMutableTreeNode child2 = new DefaultMutableTreeNode("Child 2");
            child2.add(new DefaultMutableTreeNode("Child 21"));
            child2.add(new DefaultMutableTreeNode("Child 22"));
            child2.add(new DefaultMutableTreeNode("Child 23"));
            child2.add(new DefaultMutableTreeNode("Child 24"));
            root.add(child2);
            root.add(new DefaultMutableTreeNode("Child 3"));
            root.add(new DefaultMutableTreeNode("Child 4"));
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public java.util.List<Integer> getItems() {
            return items;
        }

        public void setItems(java.util.List<Integer> items) {
            this.items = items;
        }

        public TreeModel getTreeModel() {
            return treeModel;
        }

        public void setTreeModel(TreeModel treeModel) {
            this.treeModel = treeModel;
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }
    }

}
