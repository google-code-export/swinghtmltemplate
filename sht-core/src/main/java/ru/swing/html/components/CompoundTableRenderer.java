package ru.swing.html.components;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 13.04.11
 * Time: 18:05
 */
public class CompoundTableRenderer implements TableCellRenderer {

    private Map<Pair, TableCellRenderer> rendererMap = new HashMap<Pair, TableCellRenderer>();
    private TableCellRenderer defaultRenderer;
    private TableCellRenderer currentRenderer;

    public void addRenderer(TableCellRenderer renderer, int row, int column) {
        Pair pair = new Pair(row, column);
        rendererMap.put(pair, renderer);
    }


    public TableCellRenderer getDefaultRenderer() {
        return defaultRenderer;
    }

    public void setDefaultRenderer(TableCellRenderer defaultRenderer) {
        this.defaultRenderer = defaultRenderer;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Pair pair = new Pair(row, column);

        if (rendererMap.containsKey(pair)) {
            currentRenderer = rendererMap.get(pair);
        }
        else {
            currentRenderer = defaultRenderer;
        }

        return currentRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
