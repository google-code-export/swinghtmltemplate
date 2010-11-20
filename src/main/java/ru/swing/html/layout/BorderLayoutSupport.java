package ru.swing.html.layout;

import org.apache.commons.lang.StringUtils;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.awt.*;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 11:32:52
 * </pre>
 */
public class BorderLayoutSupport implements LayoutManagerSupport {



    public void addComponent(JComponent parent, JComponent child, String constraint) {
        if ("left".equals(constraint)) {
            parent.add(child, BorderLayout.WEST);
        }
        else if ("center".equals(constraint)) {
            parent.add(child, BorderLayout.CENTER);
        }
        else if ("right".equals(constraint)) {
            parent.add(child, BorderLayout.EAST);
        }
        else if ("bottom".equals(constraint)) {
            parent.add(child, BorderLayout.SOUTH);
        }
        else if ("top".equals(constraint)) {
            parent.add(child, BorderLayout.NORTH);
        }
        else {
            parent.add(child);
        }
    }


    public LayoutManager createLayout(Tag tag) {

        int hgap = 0;
        int vgap = 0;
        String marginStyle = tag.getMargin();
        if (StringUtils.isNotEmpty(marginStyle)) {
            String[] tokens = marginStyle.split(" ");
            hgap = new Integer(tokens[0].trim());
            vgap = new Integer(tokens[1].trim());
        }
        return new BorderLayout(hgap, vgap);
    }


}
