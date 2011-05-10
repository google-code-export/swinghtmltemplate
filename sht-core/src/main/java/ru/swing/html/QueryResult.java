package ru.swing.html;

import ru.swing.html.tags.Tag;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Holds tag collection, returned after querying dom model with some selector.
 *
 * @see DomModel#query(String)
 */
public class QueryResult implements Iterable<Tag> {

    private List<Tag> results;

    public QueryResult(Tag[] results) {
        this.results = Arrays.asList(results);
    }

    public Iterator<Tag> iterator() {
        return results.listIterator();
    }

    public int size() {
        return results.size();
    }

    public Tag get(int index) {
        return results.get(index);
    }

    public QueryResult each(TagVisitor visitor) {
        for (Tag tag : results) {
            visitor.visit(tag);
        }
        return this;
    }

    public QueryResult attr(final String attrName, final String attrValue) {
        return each(new TagVisitor() {
            public void visit(Tag tag) {
                tag.setAttribute(attrName, attrValue);
                tag.applyAttribute(tag.getComponent(), attrName);
            }
        });
    }

    public QueryResult width(final String width) {
        return attr("width", width);
    }

    public QueryResult height(final String height) {
        return attr("height", height);
    }

    public QueryResult enabled(final String enabled) {
        return attr("enabled", enabled);
    }

}
