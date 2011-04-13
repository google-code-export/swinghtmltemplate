package ru.swing.html.example.utils;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * http://www.exampledepot.com/egs/javax.swing.table/Spinner.html
 */
public class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {
    final JSpinner spinner = new JSpinner();

    public SpinnerEditor() {
    }

    // Initializes the checkBox.
    public SpinnerEditor(String[] items) {
        spinner.setModel(new SpinnerListModel(java.util.Arrays.asList(items)));
    }

    // Prepares the checkBox component and returns it.
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        spinner.setValue(value);
        return spinner;
    }


    // Returns the spinners current value.
    public Object getCellEditorValue() {
        return spinner.getValue();
    }
}