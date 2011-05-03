package ru.swing.html.layout;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private Log logger = LogFactory.getLog(getClass());

    public void addComponent(JComponent parent, JComponent child, String constraint) {


        if ("left".equals(constraint)) {
            checkConstraint(parent, BorderLayout.WEST);
            parent.add(child, BorderLayout.WEST);
        }
        else if ("center".equals(constraint)) {
            checkConstraint(parent, BorderLayout.CENTER);
            parent.add(child, BorderLayout.CENTER);
        }
        else if ("right".equals(constraint)) {
            checkConstraint(parent, BorderLayout.EAST);
            parent.add(child, BorderLayout.EAST);
        }
        else if ("bottom".equals(constraint)) {
            checkConstraint(parent, BorderLayout.SOUTH);
            parent.add(child, BorderLayout.SOUTH);
        }
        else if ("top".equals(constraint)) {
            checkConstraint(parent, BorderLayout.NORTH);
            parent.add(child, BorderLayout.NORTH);
        }
        else {
            checkConstraint(parent, BorderLayout.CENTER);
            parent.add(child);
        }
    }


    private void checkConstraint(JComponent parent, String constraint) {
        BorderLayout layout = (BorderLayout) parent.getLayout();
        if (StringUtils.isNotEmpty(constraint) && layout.getLayoutComponent(parent, constraint)!=null) {
            logger.warn("Component already has a child in '"+constraint+"' align position. It will be overplaced with a new child.");
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
