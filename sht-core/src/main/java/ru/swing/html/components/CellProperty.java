package ru.swing.html.components;

import org.jdesktop.beansbinding.PropertyHelper;

import javax.swing.*;

/**
 * Helper class for TableCellBinding.
 */
public class CellProperty extends PropertyHelper {

    private int row;
    private int column;

    public CellProperty(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public Class getWriteType(Object source) {
        return Object.class;
    }

    @Override
    public Object getValue(Object source) {
        JTable table = (JTable) source;
        return table.getValueAt(row, column);
    }

    @Override
    public void setValue(Object source, Object value) {
        JTable table = (JTable) source;
        table.setValueAt(value, row, column);
    }

    @Override
    public boolean isReadable(Object source) {
        return true;
    }

    @Override
    public boolean isWriteable(Object source) {
        JTable table = (JTable) source;
        return table.isCellEditable(row, column);
    }
}
