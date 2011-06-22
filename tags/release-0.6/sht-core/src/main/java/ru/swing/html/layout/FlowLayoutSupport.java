package ru.swing.html.layout;

import org.apache.commons.lang.StringUtils;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.awt.*;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 11:51:10
 * </pre>
 */
public class FlowLayoutSupport implements LayoutManagerSupport{


    public void addComponent(JComponent parent, JComponent child, Tag childTag, String constraint) {
        parent.add(child);
    }


    public LayoutManager createLayout(Tag tag) {

        int align = FlowLayout.CENTER;
        final String styleAlign = tag.getTextAlign();
        if ("center".equals(styleAlign)) {
            align = FlowLayout.CENTER;
        }
        else if ("left".equals(styleAlign)) {
            align = FlowLayout.LEFT;
        }
        else if ("right".equals(styleAlign)) {
            align = FlowLayout.RIGHT;
        }

        int hgap = 0;
        int vgap = 0;
        String marginStyle = tag.getMargin();
        if (StringUtils.isNotEmpty(marginStyle)) {
            String[] tokens = marginStyle.split(" ");
            hgap = new Integer(tokens[0].trim());
            vgap = new Integer(tokens[1].trim());
        }

        return new FlowLayout(align, hgap, vgap);
    }
}
