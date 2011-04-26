package ru.swing.html.css;

import org.apache.commons.lang.StringUtils;
import ru.swing.html.tags.Tag;

import java.util.*;

/**
 * Описывает условие, налагаемое на аттрибуты тега в токене селектора.
 * Содержит название атрибута, его значение (может быть null) и условие на совпадение.
 * Например, для
 * <pre>
 * a[name='foo']
 * </pre>
 * значения полей:
 * <ul>
 *    <li>название - name</li>
 *    <li>значение - foo</li>
 *    <li>условие на совпадение - AttributeConstraint.EQUALS</li>
 * </ul>
 *
 */
public class AttributeMatcher {

    private String name;
    private List<String> values;
    private AttributeConstraint constraint;

    public AttributeMatcher(String name, String value, AttributeConstraint constraint) {
        this.name = name;
        //переданное значение разбиваем на подстроки по пробелу, для того, чтобы
        //проверять совпадение на наличие
        values = extractValues(value);
        this.constraint = constraint;
    }

    /**
     * Разбивает строку на подстроки по пробелу.
     * @param value строка
     * @return список подстрок или пустой список, если строка - пустая или null
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
