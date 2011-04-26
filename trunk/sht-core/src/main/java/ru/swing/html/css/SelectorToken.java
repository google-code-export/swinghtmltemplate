package ru.swing.html.css;

import org.apache.commons.lang.StringUtils;
import ru.swing.html.tags.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* <pre>
* User: Penkov Vladimir
* Date: 22.11.2010
* Time: 12:50:53
* </pre>
*/
public class SelectorToken {
    static Pattern tokenPattern = Pattern.compile("(\\w+)?(\\#(\\w+))?((\\.\\w+)*)");

    private String name;
    private List<AttributeMatcher> attributeMatchers = new ArrayList<AttributeMatcher>();

    public SelectorToken() {
    }

    public SelectorToken(String selector) {
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AttributeMatcher> getAttributeMatchers() {
        return attributeMatchers;
    }

    public void setAttributeMatchers(List<AttributeMatcher> attributeMatchers) {
        this.attributeMatchers = attributeMatchers;
    }

    public boolean matches(Tag tag) {

        if (!"*".equals(name) && !name.equals(tag.getName())) {
            return false;
        }

        for (AttributeMatcher m : attributeMatchers) {
            if (!m.matches(tag)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "SelectorToken{" +
                "name='" + name + '\'' +
                ", attributeMatchers=" + attributeMatchers +
                '}';
    }
}
