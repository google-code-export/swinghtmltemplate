package ru.swing.html.css;

import org.apache.commons.lang.StringUtils;
import ru.swing.html.tags.Tag;

import java.util.*;

/**
 * Group of selectors, separated by commas.
 */
public class SelectorGroup {

    private List<Selector> selectors;

    public SelectorGroup(String selector) {
        String[] selectorsTokens = selector.split(",");
        selectors = new ArrayList<Selector>();
        for (String token : selectorsTokens) {
            selectors.add(new Selector(token));
        }
    }

    public String getSelector(String selector) {
        return selector;
    }


    /**
     * Check if the tag matches the selector
     * @param tag tag
     * @return true, if tag matches selector, false otherwise
     */
    public boolean matches(Tag tag) {

        for (Selector token : selectors) {
            if (token.matches(tag)) {
                return true;
            }

        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SelectorGroup selector = (SelectorGroup) o;

        if (selectors != null ? !selectors.equals(selector.selectors) : selector.selectors != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return selectors != null ? selectors.hashCode() : 0;
    }


    public String toString() {
        return StringUtils.join(selectors.iterator(), ", ");
    }
}
