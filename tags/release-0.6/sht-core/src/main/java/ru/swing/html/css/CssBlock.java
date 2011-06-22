package ru.swing.html.css;

import org.apache.commons.lang.StringUtils;
import ru.swing.html.tags.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The block with css styles.
 *
 * Example:
 *
 * .foo, .foo1 {
 *     attr1: val1;
 * }
 *
 * Here ".foo" and ".foo1" are two selectors, <attr1, val1> is the style map.
 *
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


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.join(selectors.iterator(), ","));
        sb.append(" {");
        for (String attrName : styles.keySet()) {
            sb.append(attrName).append(": ").append(styles.get(attrName)).append("; ");
        }
        sb.append("}");
        return sb.toString();
    }
}
