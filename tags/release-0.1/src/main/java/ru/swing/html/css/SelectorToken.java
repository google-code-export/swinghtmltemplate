package ru.swing.html.css;

import org.apache.commons.lang.StringUtils;

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
    private String id;
    private String[] classes;

    public SelectorToken(String selector) {
        Matcher m = tokenPattern.matcher(selector.trim());
        if (m.matches()) {
            name = m.group(1);
            id = m.group(3);
            String classesString = m.group(4);
            if (StringUtils.isNotEmpty(classesString)) {
                //удаляем начальную точку
                classesString = classesString.substring(1);
                //оставшуюся строку делим по точке, токены - это названия классов 
                classes = classesString.split("\\.");
            }
        }
    }

    public String[] getClasses() {
        return classes;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
