package ru.swing.html.example.utils;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class TextAreaEditor extends AbstractCellEditor implements TableCellEditor {

    final JTextArea textArea;
    private final JScrollPane scroll;

    public TextAreaEditor() {
        textArea = new JTextArea();
        scroll = new JScrollPane(textArea);
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        textArea.setText(String.valueOf(value));
        return scroll;
    }


    public Object getCellEditorValue() {
        return textArea.getText();
    }
}