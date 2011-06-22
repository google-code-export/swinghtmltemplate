package ru.swing.html.components;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

/**
 * This cell editor delegates editing to the editor supplied for the concrete cell.
 */
public class CompoundTableEditor extends AbstractCellEditor implements TableCellEditor {

    private Map<Pair, TableCellEditor> editorMap = new HashMap<Pair, TableCellEditor>();
    private TableCellEditor defaultEditor;
    private TableCellEditor currentEditor;
    private int clickCountToStartEdit = 1;
    private StopEditingEditorListener editorListener = new StopEditingEditorListener();

    public void addEditor(TableCellEditor editor, int row, int column) {
        Pair pair = new Pair(row, column);
        editor.addCellEditorListener(editorListener);
        editorMap.put(pair, editor);
    }


    public TableCellEditor getDefaultEditor() {
        return defaultEditor;
    }

    public void setDefaultEditor(TableCellEditor defaultEditor) {
        this.defaultEditor = defaultEditor;
        defaultEditor.addCellEditorListener(editorListener);
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        Pair pair = new Pair(row, column);

        if (editorMap.containsKey(pair)) {
            currentEditor = editorMap.get(pair);
        }
        else {
            currentEditor = defaultEditor;
        }

        return currentEditor.getTableCellEditorComponent(table, value, isSelected, row, column);
    }

    public Object getCellEditorValue() {
        return currentEditor.getCellEditorValue();
    }


    @Override
    public boolean stopCellEditing() {
        if (currentEditor!=null) {
            return currentEditor.stopCellEditing();
        }
        return super.stopCellEditing();
    }

    @Override
    public void cancelCellEditing() {
        if (currentEditor!=null) {
            currentEditor.cancelCellEditing();
        }
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return currentEditor.shouldSelectCell(anEvent);
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        stopCellEditing();
        if (e instanceof MouseEvent) {
            return ((MouseEvent)e).getClickCount() >= clickCountToStartEdit && super.isCellEditable(e);
        }
        return super.isCellEditable(e);
    }


    private class StopEditingEditorListener implements CellEditorListener {
        public void editingStopped(ChangeEvent e) {
            fireEditingStopped();
        }

        public void editingCanceled(ChangeEvent e) {
            fireEditingCanceled();
        }
    }

}
