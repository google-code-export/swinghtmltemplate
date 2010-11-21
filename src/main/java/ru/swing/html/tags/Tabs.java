package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.DomConverter;

import javax.swing.*;

/**
 * <p>
 * Тег преобразуется в панель JTabbedPane.
 * </p>
 * <p>
 * Для каждого дочернего тега создается вкладка, в качестве названия
 * подставляется значение атрибута title дочернего тега.
 * </p>
 * <p>
 * Тег поддерживает атрибуты:
 * </p>
 * <ul>
 *   <li>
 *   <b>tab-position</b> - определяет позицию вкладок. Допустимые значения: top, bottom, left, right.
 *   </li>
 * </ul>
 */
public class Tabs extends Tag {
    
    private Log logger = LogFactory.getLog(getClass());
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
    public void applyAttributes(JComponent component) {
        super.applyAttributes(component);

        JTabbedPane tabs = (JTabbedPane) getComponent();

        String tabPlacement = getAttribute("tab-position");
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

    @Override
    public void handleChildren() {
        
        JTabbedPane tabs = (JTabbedPane) getComponent();
        
        for (Tag child : getChildren()) {
            JComponent childComponent = DomConverter.convertComponent(child);
            tabs.addTab(child.getAttribute("title"), childComponent);
        }
    }
}
