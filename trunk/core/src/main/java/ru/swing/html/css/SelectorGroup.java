package ru.swing.html.css;

import ru.swing.html.tags.Tag;

import java.util.*;

/**
 * Группа селекторов, разделенных запятыми.
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
     * Проверяет, попадает ли тег под выборку селектора.
     * @param tag тег
     * @return true, если тег попадает под выборку селектора, иначе false
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
}
