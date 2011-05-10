package ru.swing.html.tags.swing;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.DomConverter;
import ru.swing.html.ELUtils;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.util.Map;

/**
 * <p>
 * Tag is converted into JTabbedPane.
 * </p>
 * <p>
 *     For each child tag the tab is created. The tab's title is taken from the title attribute of the child.
 * </p>
 * <p>
 * Tag supports attributes:
 * </p>
 * <ul>
 *   <li>
 *   <b>tab-position</b> - sets the position of tabs. Possible values: top, bottom, left, right.
 *   </li>
 * </ul>
 */
public class Tabs extends Tag {
    
    private Log logger = LogFactory.getLog(getClass());
    public static final String TAB_POSITION_ATTRIBUTE = "tab-position";

    @Override
    public JComponent createComponent() {
        JTabbedPane c = new JTabbedPane();
        setComponent(c);
        return c;
    }

    @Override
    public void handleLayout() {
    }

    @Override
    public void applyAttribute(JComponent component, String name) {

        JTabbedPane tabs = (JTabbedPane) component;
        if (TAB_POSITION_ATTRIBUTE.equals(name)) {
            String tabPlacement = getAttribute(TAB_POSITION_ATTRIBUTE);
            int tabPlacementPos = JTabbedPane.TOP;
            if (StringUtils.isEmpty(tabPlacement) || "top".equals(tabPlacement)) {
                tabPlacementPos = JTabbedPane.TOP;
            }
            else if ("bottom".equals(tabPlacement)) {
                tabPlacementPos = JTabbedPane.BOTTOM;
            }
            else if ("left".equals(tabPlacement)) {
                tabPlacementPos = JTabbedPane.LEFT;
            }
            else if ("right".equals(tabPlacement)) {
                tabPlacementPos = JTabbedPane.RIGHT;
            }
            else {
                logger.warn("Unknown tab-position: "+tabPlacement);
            }

            tabs.setTabPlacement(tabPlacementPos);

        }
        else {
            super.applyAttribute(component, name);
        }


    }

    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {
        
        JTabbedPane tabs = (JTabbedPane) getComponent();
        
        for (Tag child : getChildren()) {
            JComponent childComponent = DomConverter.convertComponent(child, substitutions);
            if (childComponent!=null) {
                String title = ELUtils.parseStringValue(child.getAttribute("title"), getModelElements());
                tabs.addTab(title, child.getComponentWrapper());
            }
        }
    }
}
