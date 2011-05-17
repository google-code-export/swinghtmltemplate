package ru.swing.html.sandbox.dnd;

import ru.swing.html.tags.Tag;
import ru.swing.html.tags.swing.Tree;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.datatransfer.DataFlavor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 17.05.11
 * Time: 15:10
 * To change this template use File | Settings | File Templates.
 */
public class TagTransferDataProvider implements TransferDataProvider {


    public Object getTransferData(Tag tag, DataFlavor flavor) {
        if (tag.getComponent() instanceof JTree) {
            //get selected nodes and add them to flavor as array
            JTree tree = (JTree) tag.getComponent();
            if (tree==null) {
                return null;
            }

            TreePath[] selectionPaths = tree.getSelectionPaths();
            if (selectionPaths == null) {
                return null;
            }
            List<Object> selected = new ArrayList<Object>(selectionPaths.length);
            for (TreePath path : selectionPaths) {
                Object lastPathComponent = path.getLastPathComponent();
                //is node is DefaultMutableTreeNode, use its userObject, else use node itself
                if (lastPathComponent instanceof DefaultMutableTreeNode) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) lastPathComponent;
                    selected.add(node.getUserObject());
                }
                else {
                    selected.add(lastPathComponent);
                }
            }

            return selected.toArray(new Object[selected.size()]);

        }

        else if (tag.getComponent() instanceof JTable) {
            //get selected cells value
            JTable table = (JTable) tag.getComponent();

            // Get the min and max ranges of selected cells
            int rowIndexStart = table.getSelectedRow();
            int rowIndexEnd = table.getSelectionModel().getMaxSelectionIndex();
            int colIndexStart = table.getSelectedColumn();
            int colIndexEnd = table.getColumnModel().getSelectionModel().getMaxSelectionIndex();

            List<Object> selectedData = new ArrayList<Object>();

            // Check each cell in the range
            for (int r = rowIndexStart; r <= rowIndexEnd; r++) {
                for (int c = colIndexStart; c <= colIndexEnd; c++) {
                    if (table.isCellSelected(r, c)) {
                        selectedData.add(table.getValueAt(r, c));
                    }
                }
            }
            return selectedData.toArray(new Object[selectedData.size()]);
        }

        else if (tag.getComponent() instanceof JList) {
            //get selected items
            JList list = (JList) tag.getComponent();

            List<Object> selectedData = new ArrayList<Object>();

            // Check each cell in the range
            for (int c : list.getSelectedIndices()) {
                selectedData.add(list.getModel().getElementAt(c));
            }
            return selectedData.toArray(new Object[selectedData.size()]);
        }

        else if (tag.getComponent() instanceof JTextComponent) {
            //get selected text
            JTextComponent list = (JTextComponent) tag.getComponent();
            return list.getSelectedText();

        }

        return null;
    }
}
