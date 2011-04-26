package ru.swing.html.css;

import ru.swing.html.tags.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 22.11.2010
 * Time: 11:59:17
 * </pre>
 */
public class CssBlock {

    private List<SelectorGroup> selectors;
    private Map<String, String> styles = new HashMap<String, String>();

    public CssBlock(List<SelectorGroup> selectors, Map<String, String> styles) {
        this.selectors = selectors;
        this.styles = styles;
    }

    public boolean matches(Tag tag) {
        for (SelectorGroup selector : selectors) {
            final boolean match = selector.matches(tag);
            if (match) {
                return true;
            }
        }
        return false;
    }

    public Map<String, String> getStyles() {
        return styles;
    }

}
