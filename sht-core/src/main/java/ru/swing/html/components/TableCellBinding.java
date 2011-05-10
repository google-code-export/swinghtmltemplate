package ru.swing.html.components;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.beansbinding.PropertyStateEvent;
import org.jdesktop.beansbinding.PropertyStateListener;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Binding support for FormTable tag
 */
public class TableCellBinding extends AutoBinding {

    private Handler handler;
    private JTable table;
    int column;
    int row;

    public TableCellBinding(UpdateStrategy strategy,
                            Object sourceObject,
                            Property sourceProperty,
                            JTable targetObject,
                            int row, int column,
                            String name) {
        super(strategy, sourceObject, sourceProperty, targetObject, new CellProperty(row, column), name);
        this.table = targetObject;
        this.row = row;
        this.column = column;

    }

    protected void bindImpl() {
        handler = new Handler(row, column);
        table.getModel().addTableModelListener(handler);
        table.addPropertyChangeListener("model", handler);
        super.bindImpl();
    }

    protected void unbindImpl() {
        table.getModel().removeTableModelListener(handler);
        table.removePropertyChangeListener("model", handler);
        super.unbindImpl();
    }

    @Override
    protected void sourceChangedImpl(PropertyStateEvent pse) {
        handler.propertyStateChanged(pse);
    }

    class Handler implements PropertyStateListener, TableModelListener, PropertyChangeListener {

        int column;
        int row;
        private boolean isAdjusting;

        public Handler(int row, int column) {
            this.column = column;
            this.row = row;
        }

        public void propertyStateChanged(PropertyStateEvent pse) {
            isAdjusting = true;
            TableCellBinding.this.refresh();
            //table.setValueAt(pse.getNewValue(), row, column);
            //cellValueChanged();
            isAdjusting = false;
        }

        public void tableChanged(TableModelEvent e) {
            if (e.getColumn()==column && (e.getFirstRow()>=row && e.getLastRow()<=row && !isAdjusting)) {
                TableCellBinding.this.save();
            }
        }
        private void cellValueChanged() {
            TableCellBinding.this.save();
        }


        public void propertyChange(PropertyChangeEvent evt) {
        }
    }
}
