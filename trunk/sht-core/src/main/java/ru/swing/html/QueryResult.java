package ru.swing.html;

import org.apache.commons.lang.StringUtils;
import ru.swing.html.tags.Tag;

import java.util.*;

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


    public boolean hasClass(String classNames) {
        final Set<Boolean> results = new HashSet<Boolean>();

        final List<String> searchedClasses = Arrays.asList(Utils.extractParams(classNames));


        each(new TagVisitor() {
            public void visit(Tag tag) {
                if (!results.contains(Boolean.FALSE) && StringUtils.isNotEmpty(tag.getAttribute("class"))) {
                    List<String> classes = Arrays.asList(Utils.extractParams(tag.getAttribute("class")));
                    if (!classes.containsAll(searchedClasses)) {
                        results.add(false);
                    }
                }
            }
        });

        return results.contains(false);
    }

    public QueryResult addClass(final String className) {

        each(new TagVisitor() {
            public void visit(Tag tag) {
                String styleClass = tag.getAttribute("class");
                List<String> classes = new ArrayList<String>();
                if (StringUtils.isNotEmpty(styleClass)) {
                    classes.addAll(Arrays.asList(Utils.extractParams(styleClass)));
                }
                if (!classes.contains(className)) {
                    classes.add(className);
                    tag.setAttribute("class", StringUtils.join(classes.iterator(), " "));
                    tag.applyAttribute(tag.getComponent(), "class");
                }
            }
        });

        return this;
    }

    public QueryResult removeClass(final String className) {

        each(new TagVisitor() {
            public void visit(Tag tag) {
                String styleClass = tag.getAttribute("class");
                List<String> classes = new ArrayList<String>();
                if (StringUtils.isNotEmpty(styleClass)) {
                    classes.addAll(Arrays.asList(Utils.extractParams(styleClass)));
                }
                if (classes.contains(className)) {
                    classes.remove(className);
                    tag.setAttribute("class", StringUtils.join(classes.iterator(), " "));
                    tag.applyAttribute(tag.getComponent(), "class");
                }
            }
        });

        return this;
    }

    public QueryResult toggleClass(final String className) {

        each(new TagVisitor() {
            public void visit(Tag tag) {
                List<String> classes = Arrays.asList(Utils.extractParams(tag.getAttribute("class")));
                if (classes.contains(className)) {
                    classes.remove(className);
                }
                else {
                    classes.add(className);
                }
                tag.setAttribute("class", StringUtils.join(classes.iterator(), " "));
                tag.applyAttribute(tag.getComponent(), "class");
            }
        });

        return this;
    }


}
