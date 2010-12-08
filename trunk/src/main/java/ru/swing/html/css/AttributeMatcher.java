package ru.swing.html.css;

import org.apache.commons.lang.StringUtils;
import ru.swing.html.tags.Tag;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 08.12.2010
 * Time: 22:44:19
 */
public class AttributeMatcher {

    private String name;
    private List<String> values;
    private AttributeConstraint constraint;

    public AttributeMatcher(String name, String value, AttributeConstraint constraint) {
        this.name = name;
        values = extractValues(value);
        this.constraint = constraint;
    }

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
