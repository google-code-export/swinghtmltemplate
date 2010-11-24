package ru.swing.html.layout;

import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringUtils;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 21.11.2010
 * Time: 0:04:22
 */
public class MigLayoutSupport implements LayoutManagerSupport {

    public static final String X_MIGLAYOUT_CONSTRAINTS_ATTRIBUTE = "x-miglayout-constraints";
    public static final String X_MIGLAYOUT_ROW_CONSTRAINTS_ATTRIBUTE = "x-miglayout-row-constraints";
    public static final String X_MIGLAYOUT_COLUMN_CONSTRAINTS_ATTRIBUTE = "x-miglayout-column-constraints";

    public void addComponent(JComponent parent, JComponent child, String constraint) {
        parent.add(child, constraint);
    }

    public LayoutManager createLayout(Tag tag) {
        MigLayout migLayout = new MigLayout();
        String layoutConstraints = tag.getAttribute(X_MIGLAYOUT_CONSTRAINTS_ATTRIBUTE);
        if (StringUtils.isNotEmpty(layoutConstraints)) {
            migLayout.setLayoutConstraints(layoutConstraints);
        }
        String rowConstraints = tag.getAttribute(X_MIGLAYOUT_ROW_CONSTRAINTS_ATTRIBUTE);
        if (StringUtils.isNotEmpty(rowConstraints)) {
            migLayout.setRowConstraints(rowConstraints);
        }
        String columnConstraints = tag.getAttribute(X_MIGLAYOUT_COLUMN_CONSTRAINTS_ATTRIBUTE);
        if (StringUtils.isNotEmpty(columnConstraints)) {
            migLayout.setColumnConstraints(columnConstraints);
        }
        return migLayout;
    }
}
