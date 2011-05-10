package ru.swing.html.tags.swing;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.DomConverter;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Tag is converted into JSplitPane.
 * </p>
 * <p>
 * Supported attributes:
 * </p>
 * <ul>
 * <li>orientation - orientation of JSplitPane, possible values: horizontal, vertical</li>
 * <li>divider-size - the size of the divider</li>
 * <li>divider-position - the position of the divider. must be set in pizels (100) or in persents (50%)</li>
 * </ul>
 *
 * <p>
 *     Tag can contain 2 child tags. Their position is based on "align" attribute of each child.
 * </p>
 * <ul>
 * <li>left - component goes to left panel</li>
 * <li>top - component goes to top panel</li>
 * <li>right - component goes to right panel</li>
 * <li>bottom - component goes to bottom panel</li>
 * </ul>
 * <p>
 * If no "align" is set in the first tag, then first tag goes to left, 2nd - to right.
 * </p>
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 16:55:57
 * </pre>
 */
public class SplitPane extends Tag {
    private Log logger = LogFactory.getLog(getClass());
    private int orientation;
    private int dividerWidth;
    private Double dividerPercentLocation = null;
    private int dividerLocation = -1;
    public static final String ORIENTATION_ATTRIBUTE = "orientation";
    public static final String DIVIDER_SIZE_ATTRIBUTE = "divider-size";
    public static final String DIVIDER_POSITION_ATTRIBUTE = "divider-position";

    @Override
    public String getName() {
        return "split";
    }

    @Override
    public JComponent createComponent() {
        JSplitPane c = new JSplitPane();
        setComponent(c);
        return c;
    }
    
    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {
        if (getComponent() instanceof JSplitPane) {

            JSplitPane pane = (JSplitPane) getComponent();

            List<Tag> childrenWithComponents = new ArrayList<Tag>();
            for (Tag child : getChildren()) {
                JComponent childComponent = DomConverter.convertComponent(child, substitutions);
                if (childComponent!=null) {
                    childrenWithComponents.add(child);
                }
            }

            if (childrenWithComponents.size()<=2) {
                JComponent childComponent1 = childrenWithComponents.size()>0 ? childrenWithComponents.get(0).getComponentWrapper() : null;
                JComponent childComponent2 = childrenWithComponents.size()>1 ? childrenWithComponents.get(1).getComponentWrapper() : null;

                if (childComponent1!=null) {
                    placeChild(childComponent1, childrenWithComponents.get(0).getAlign(), "left");
                    if (childComponent2!=null) {
                        //2nd is placed into free cell. So if the 1st tag went to the right panel, 2nd will go to the left
                        if (pane.getLeftComponent()!=null) {
                            placeChild(childComponent2, childrenWithComponents.get(1).getAlign(), "right");
                        }
                        else {
                            placeChild(childComponent2, childrenWithComponents.get(1).getAlign(), "left");
                        }
                    }
                }

            }

            if (childrenWithComponents.size()>2) {
                logger.warn(getName()+" must not contain more than 2 children");
            }
        }
        else {
            logger.fatal("Expected component of type "+JSplitPane.class+" but recieved: "+getContent().getClass());
        }
    }

    private void placeChild(JComponent child, String position, String defaultPosition) {
        JSplitPane pane = (JSplitPane) getComponent();
        if (position==null) {
            position = defaultPosition;
        }

        if ("left".equals(position)) {
            pane.setLeftComponent(child);
        }
        else if ("right".equals(position)) {
            pane.setRightComponent(child);
        }
        else if ("top".equals(position)) {
            pane.setTopComponent(child);
        }
        else if ("bottom".equals(position)) {
            pane.setBottomComponent(child);
        }
        else {
            logger.warn("Unknown position: "+position);
        }
    }

    @Override
    public void applyAttribute(JComponent component, String name) {
        if (component instanceof JSplitPane) {

            JSplitPane pane = (JSplitPane) component;


            if (ORIENTATION_ATTRIBUTE.equals(name)) {
                pane.setOrientation(orientation);
            }
            else if (DIVIDER_SIZE_ATTRIBUTE.equals(name)) {
                pane.setDividerSize(dividerWidth);
            }
            else if (DIVIDER_POSITION_ATTRIBUTE.equals(name)) {
                if (dividerPercentLocation!=null) {
                    pane.setDividerLocation(dividerPercentLocation);
                }
                else if (dividerLocation>=0) {
                    pane.setDividerLocation(dividerLocation);
                }
            }
            else {
                super.applyAttribute(component, name);
            }
        }
        else {
            logger.fatal("Expected component of type "+JSplitPane.class+" but recieved: "+component.getClass());
        }
    }

    @Override
    public void setAttribute(String name, String value) {
        if (ORIENTATION_ATTRIBUTE.equals(name)) {
            if ("vertical".equals(value)) {
                orientation = JSplitPane.VERTICAL_SPLIT;
            }
            else if ("horizontal".equals(value)) {
                orientation = JSplitPane.HORIZONTAL_SPLIT;
            }
            else if (StringUtils.isEmpty(value)) {
                orientation = JSplitPane.HORIZONTAL_SPLIT;
            }
            else {
                logger.warn("Unknown orientation: "+value);
            }
        }
        else if (DIVIDER_SIZE_ATTRIBUTE.equals(name)) {
            dividerWidth = new Integer(value);
        }
        else if (DIVIDER_POSITION_ATTRIBUTE.equals(name)) {
            final int index = value.indexOf("%");
            if (index !=-1) {
                String val = value.substring(0, index);
                dividerPercentLocation = new Integer(val)/100d;
            }
            else {
                dividerLocation = new Integer(value);
            }
        }
        super.setAttribute(name, value);
    }
}
