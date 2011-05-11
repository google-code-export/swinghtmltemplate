package ru.swing.html.example.utils;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 12.04.11
 * Time: 18:48
 */
public class ColorTableCellRenderer extends JPanel implements TableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setBackground((Color) value);
        if (hasFocus) {
            setBorder(UIManager.getBorder("Table.focusHightlightBorder"));
        }
        else {
            setBorder(null);
        }
        return this;
    }
}
