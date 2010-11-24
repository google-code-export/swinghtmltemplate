package ru.swing.html.layout;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 21.11.2010
 * Time: 0:39:20
 * To change this template use File | Settings | File Templates.
 */
public class BoxLayoutSupport implements LayoutManagerSupport {

    private Log logger = LogFactory.getLog(getClass());

    public void addComponent(JComponent parent, JComponent child, String constraint) {
        parent.add(child);
    }

    public LayoutManager createLayout(Tag tag) {

        String direction = tag.getAttribute("x-boxlayout-direction");
        int d = BoxLayout.X_AXIS;
        if ("horizontal".equals(direction)) {
            d = BoxLayout.X_AXIS;
        }
        else if ("vertical".equals(direction)) {
            d = BoxLayout.Y_AXIS;
        }
        else if (StringUtils.isEmpty(direction)) {
            d = BoxLayout.X_AXIS;
        }
        else {
            logger.warn("Unknown direction: " + direction);
        }

        BoxLayout l = new BoxLayout(tag.getComponent(), d);
        return l;
    }
}
