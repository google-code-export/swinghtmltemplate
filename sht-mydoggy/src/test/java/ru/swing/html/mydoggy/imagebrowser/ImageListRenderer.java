package ru.swing.html.mydoggy.imagebrowser;

import ru.swing.html.*;

import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;

public class ImageListRenderer extends DefaultListCellRenderer implements ListCellRenderer {

    private int thumbnailSize = 100;
    private Integer innerInset = 5;
    private Integer outerInset = 3;
    private Integer borderWidth = 1;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(outerInset, outerInset, outerInset, outerInset),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.black, borderWidth),
                        BorderFactory.createEmptyBorder(innerInset, innerInset, innerInset, innerInset))));

        File file = (File) value;
        Integer border = outerInset * 2 + innerInset * 2 + borderWidth*2;
        label.setPreferredSize(new Dimension(thumbnailSize + border, thumbnailSize + border + 30));
        label.setText(file.getName());
        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();

        if (width>200 || height>200) {
            icon = new ImageIcon(getClass().getClassLoader().getResource("ru/swing/html/mydoggy/imagebrowser/default_thumb.gif"));
        }

        if (width>height) {
            width = Math.min(thumbnailSize, width);
            height = -1;
        }
        else {
            width = -1;
            height = Math.min(thumbnailSize, height);
        }
        label.setIcon(new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_FAST)));
        label.setVerticalTextPosition(BOTTOM);
        label.setHorizontalTextPosition(CENTER);
//        label.setIcon(icon);
        return label;
    }

    public int getThumbnailSize() {
        return thumbnailSize;
    }

    public void setThumbnailSize(int thumbnailSize) {
        this.thumbnailSize = thumbnailSize;
    }
}
