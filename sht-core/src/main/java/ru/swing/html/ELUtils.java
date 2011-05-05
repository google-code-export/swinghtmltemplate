package ru.swing.html;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.ELProperty;

import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ELUtils {

    private static Log logger = LogFactory.getLog(ELUtils.class);

    public static final String placeholderPrefix = "${";
    public static final String placeholderSuffix = "}";
    public static final boolean ignoreUnresolvablePlaceholders = false;


    /**
     * Parses string for a placeholders recursively. The placeholders are evaled against
     * model.
     * @param original original string to parse
     * @param model model, containing values to be used for placeholders
     * @return parsed string
     */
    public static String parseStringValue(String original, Map<String, Object> model) {
        if (StringUtils.isEmpty(original)) {
            return original;
        }
        return parseStringValue(original, model, new HashSet());
    }


    /**
     * Parse the given String value recursively, to be able to resolve
     * nested placeholders (when resolved property values in turn contain
     * placeholders again).
     * @param strVal the String value to parse
     * @param visitedPlaceholders the placeholders that have already been visited
     * during the current resolution attempt (used to detect circular references
     * between placeholders). Only non-null if we're parsing a nested placeholder.
     */
    protected static String parseStringValue(String strVal, Map<String, Object> model, Set visitedPlaceholders) {

        StringBuffer buf = new StringBuffer(strVal);

        int startIndex = strVal.indexOf(ELUtils.placeholderPrefix);
        while (startIndex != -1) {
            int endIndex = findPlaceholderEndIndex(buf, startIndex);
            if (endIndex != -1) {
                String placeholder = buf.substring(startIndex + ELUtils.placeholderPrefix.length(), endIndex);
                if (!visitedPlaceholders.add(placeholder)) {
                    throw new IllegalArgumentException("Circular placeholder reference '" + placeholder + "' in property definitions");
                }
                // Recursive invocation, parsing placeholders contained in the placeholder key.
                placeholder = parseStringValue(placeholder, model, visitedPlaceholders);
                // Now obtain the value for the fully resolved key...
                String propVal = resolvePlaceholder(placeholder, model);
                if (propVal != null) {
                    // Recursive invocation, parsing placeholders contained in the
                    // previously resolved placeholder value.
                    propVal = parseStringValue(propVal, model, visitedPlaceholders);
                    buf.replace(startIndex, endIndex + ELUtils.placeholderSuffix.length(), propVal);
                    if (logger.isTraceEnabled()) {
                        logger.trace("Resolved placeholder '" + placeholder + "'");
                    }
                    startIndex = buf.indexOf(ELUtils.placeholderPrefix, startIndex + propVal.length());
                }
                else if (ELUtils.ignoreUnresolvablePlaceholders) {
                    // Proceed with unprocessed value.
                    startIndex = buf.indexOf(ELUtils.placeholderPrefix, endIndex + ELUtils.placeholderSuffix.length());
                }
                else {
                    throw new IllegalArgumentException("Could not resolve placeholder '" + placeholder + "'");
                }
                visitedPlaceholders.remove(placeholder);
            }
            else {
                startIndex = -1;
            }
        }

        return buf.toString();
    }



    protected static String resolvePlaceholder(String placeholder, Map<String, Object> model) {
        ELProperty p = ELProperty.create(placeholderPrefix+placeholder+placeholderSuffix);
        Object o = null;
        if (p.isReadable(model)) {
            o = p.getValue(model);
        }

        String propVal = o!=null ? o.toString() : null;
        return propVal;
    }



    private static int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
        int index = startIndex + ELUtils.placeholderPrefix.length();
        int withinNestedPlaceholder = 0;
        while (index < buf.length()) {
            if (substringMatch(buf, index, ELUtils.placeholderSuffix)) {
                if (withinNestedPlaceholder > 0) {
                    withinNestedPlaceholder--;
                    index = index + ELUtils.placeholderSuffix.length();
                }
                else {
                    return index;
                }
            }
            else if (substringMatch(buf, index, ELUtils.placeholderPrefix)) {
                withinNestedPlaceholder++;
                index = index + ELUtils.placeholderPrefix.length();
            }
            else {
                index++;
            }
        }
        return -1;
    }


    /**
     * Test whether the given string matches the given substring
     * at the given index.
     * @param str the original string (or StringBuffer)
     * @param index the index in the original string to start matching against
     * @param substring the substring to match at the given index
     */
    public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
        for (int j = 0; j < substring.length(); j++) {
            int i = index + j;
            if (i >= str.length() || str.charAt(i) != substring.charAt(j)) {
                return false;
            }
        }
        return true;
    }

}
