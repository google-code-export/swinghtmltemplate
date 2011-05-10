package ru.swing.html.css;

import org.apache.commons.lang.StringUtils;
import ru.swing.html.tags.Tag;

import java.util.*;

/**
 * Describes the condition for tag attribute in the selector token.
 * Holds attribute name, it's value (can be null) and condition for the match.
 * For example,
 * <pre>
 * a[name='foo']
 * </pre>
 * these are:
 * <ul>
 *    <li>name - name</li>
 *    <li>value - foo</li>
 *    <li>condition for the match - AttributeConstraint.EQUALS</li>
 * </ul>
 *
 */
public class AttributeMatcher {

    private String name;
    private List<String> values;
    private AttributeConstraint constraint;

    public AttributeMatcher(String name, String value, AttributeConstraint constraint) {
        this.name = name;
        //split value into substring to allow checking 'exist' condition
        values = extractValues(value);
        this.constraint = constraint;
    }

    /**
     * Splits string into substring with a space as separator
     * @param value source string
     * @return collection of substrings or empty collection, if source string is empty or null
     */
    private List<String> extractValues(String value) {
        List<String> values;
        if (StringUtils.isEmpty(value)) {
            values =  Collections.emptyList();
        }
        else {
            values = Arrays.asList(value.split(" "));
        }
        return values;
    }

    public boolean matches(Tag tag) {
        switch (constraint) {
            case EQUALS:
                return values.size()==1 && values.iterator().next().equals(tag.getAttribute(name));
            case HAS_ATTRIBUTE:
                return tag.getAttribute(name)!=null;
            case HAS_VALUE:
                if (values.isEmpty()) {
                    return false;
                }
                List<String> value = extractValues(tag.getAttribute(name));
                return !value.isEmpty() && value.containsAll(values);
            case STARTS_WITH:
                String tagValue = tag.getAttribute(name);
                if (values.size()!=1 || StringUtils.isEmpty(tagValue)) {
                    return false;
                }

                String val = values.iterator().next();
                return tagValue.equals(val) || tagValue.startsWith(val+"-");
        }
        return false;
    }

    @Override
    public String toString() {
        return "AttributeMatcher{" +
                "name='" + name + '\'' +
                ", values=" + values +
                ", constraint=" + constraint +
                '}';
    }
}
