package ru.swing.html.css;

import org.apache.commons.lang.StringUtils;
import ru.swing.html.tags.Tag;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Поддерживаемые типы селекторов:
 * p#id.class1.class2, p.class3, .class4, #id, 
 */
public class Selector {

    private List<SelectorToken> selectors;

    public Selector(String selector) {
        String[] selectorsTokens = selector.split(",");
        selectors = new ArrayList<SelectorToken>();
        for (String token : selectorsTokens) {
            selectors.add(new SelectorToken(token));
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

        for (SelectorToken token : selectors) {
            boolean res = true;

            if (StringUtils.isNotEmpty(token.getName()) && !"*".equals(token.getName()) && !token.getName().equals(tag.getName())) {
                res = false;
            }

            if (StringUtils.isNotEmpty(token.getId()) && !token.getId().equals(tag.getId())) {
                res = false;
            }

            if (token.getClasses()!=null) {
                String classStr = tag.getAttribute("class");
                if (StringUtils.isNotEmpty(classStr)) {
                    String[] classes = classStr.split(" ");
                    //нужно чтобы все классы, указанные в селекторе, присутствовали в атрибуте class тега
                    if (classes!=null) {
                        Set<String> classesSet = new HashSet<String>(Arrays.asList(classes));
                        
                        if (!classesSet.containsAll(Arrays.asList(token.getClasses()))) {
                            res = false;
                        }
                    }
                }
                else {//если у тега не указан класс, то он точно не попадает
                    res = false;
                }
            }

            if (res) {
                return true;
            }
        }

        return false;
    }


}
