package ru.swing.html.xhtmlrenderer;

import org.apache.commons.lang.StringUtils;
import ru.swing.html.css.StyleParser;

import java.util.Map;

/**
 * Utils for xhtmlrenderer.
 */
public class Utils {


    /**
     * Modifies css style string so 'attrName' attribute is set to 'attrValue'. If there is no
     * such attribute, it is added.
     *
     * <h2>Example:</h2>
     *
     * <p>let attrName=='display', attrValue='block'</p>
     *
     * <pre>
     * display: border; text-align:left;
     * </pre>
     * is converted to:
     * <pre>
     * display: block; text-align:left;
     * </pre>
     * and
     * <pre>
     * text-align:left;
     * </pre>
     * is converted to:
     * <pre>
     * display: block; text-align:left;
     * </pre>
     *
     * @param originalStyle original string with css style
     * @param attrName name of the attribute to add/modify
     * @param attrValue the value of the attrubute
     * @return modified css style with added or replaced attribute
     */
    public static String replaceDisplay(String originalStyle, String attrName, String attrValue) {
        if (StringUtils.isEmpty(originalStyle)) {
            return attrName+": "+attrValue+";";
        }
        Map<String, String> styles = StyleParser.extractStyles(originalStyle);
        styles.put(attrName, attrValue);
        StringBuilder modifiedStyle = new StringBuilder();
        for (String key : styles.keySet()) {
            modifiedStyle.append(key).append(":").append(styles.get(key)).append(";");
        }
        return modifiedStyle.toString();
    }





}
