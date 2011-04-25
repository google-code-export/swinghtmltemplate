package ru.swing.html.example.nbnewproject;

import javax.swing.*;
import java.awt.*;

/**
 * Renderer for projects list
 */
public class ProjectTypeRenderer extends DefaultListCellRenderer implements ListCellRenderer {

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        ProjectType project = (ProjectType) value;
        label.setText(project.getName());
        label.setIcon(new ImageIcon(getClass().getResource(project.getIcon())));
        return label;
    }
}
