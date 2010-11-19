package ru.swing.html;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 12:33:05
 * </pre>
 */
public class StyleParser {

    /**
     * Разбивает строку
     * <pre>
     * attr1 : val1;
     * attr2 : val2;
     * </pre>
     * на карту <Имя, Значение>
     * @param style строка стиля
     * @return карта
     */
    public static Map<String, String> extractStyles(String style) {

        Map<String, String> result = new HashMap<String, String>();
        if (StringUtils.isEmpty(style)) {
            return result;
        }
        String[] tokens = style.split(";");
        for (String token : tokens) {
            if (!StringUtils.isEmpty(token)) {
                String[] nameVal = token.split(":");
                String name = nameVal[0].trim();
                String val = nameVal[1].trim();
                result.put(name, val);
            }
        }
        return result;
    }

}
