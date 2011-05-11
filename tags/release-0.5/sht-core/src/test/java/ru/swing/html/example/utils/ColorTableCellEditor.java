package ru.swing.html.example.utils;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EventObject;

/**
 * Table cell editor for Color.
 */
public class ColorTableCellEditor extends AbstractCellEditor implements TableCellEditor{

    private JColorChooser colorChooser;
    private JDialog colorDialog;
    private JPanel panel;
    private ActionListener cancelListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            cancelCellEditing();
        }
    };
    private ActionListener okListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            stopCellEditing();
        }
    };
    private WindowAdapter closeListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            cancelCellEditing();
        }
    };
    private String dialogTitle = "Choose color";

    public ColorTableCellEditor() {
        panel = new JPanel();
        colorChooser = new JColorChooser();
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        colorDialog = JColorChooser.createDialog(table, dialogTitle, true, colorChooser, okListener, cancelListener);
        colorDialog.addWindowListener(closeListener);
        colorChooser.setColor((Color) value);
        return panel;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        if (colorDialog!=null) {
            colorDialog.setVisible(true);
        }
        return true;
    }

    @Override
    public void cancelCellEditing() {
        if (colorDialog!=null) {
            colorDialog.setVisible(false);
        }
        super.cancelCellEditing();
    }

    @Override
    public boolean stopCellEditing() {
        if (colorDialog!=null) {
            colorDialog.setVisible(false);
        }
        super.stopCellEditing();
        return true;
    }

    public Object getCellEditorValue() {
        return colorChooser.getColor();
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }
}
