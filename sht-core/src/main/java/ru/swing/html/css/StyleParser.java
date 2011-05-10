package ru.swing.html.css;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 12:33:05
 * </pre>
 */
public class StyleParser {
    private static final Pattern CSS_BLOCK_PATTERN = Pattern.compile("(.*?\\{.*?\\})", Pattern.DOTALL+Pattern.MULTILINE);
    private static final Pattern SELECTOR_PATTERN = Pattern.compile("(.*?)\\{(.*?)\\}", Pattern.DOTALL+Pattern.MULTILINE);

    private static Log logger = LogFactory.getLog(StyleParser.class);

    /**
     * Splits the string of css styles into blocks. Each block corresponds to 1 style.
     * <pre>
     * .foo1 { nam1: val1} foo2 { nam2: val2 }
     * </pre>
     * will be splitted into 2 blocks.
     * @param css the string with styles
     * @return collection of block of styles
     */
    public static List<String> extractCssBlocks(String css) {
        Matcher m = CSS_BLOCK_PATTERN.matcher(css);
        List<String> res = new ArrayList<String>();
        while (m.find()) {
            res.add(m.group(1).trim());
        }
        return res;
    }


    /**
     * Extracts the information about styles from css block
     * @param cssBlock the string containing css block
     * @return information about css block or null, if it can't be extracted
     */
    public static CssBlock parseCssBlock(String cssBlock) {
        Matcher m = SELECTOR_PATTERN.matcher(cssBlock);
        if (m.matches()) {
            String selectorsString = m.group(1);
            String styles = m.group(2);
            String[] selectorsArray = selectorsString.split(",");
            List<SelectorGroup> selectors = new ArrayList<SelectorGroup>();
            for (String s : selectorsArray) {
                selectors.add(new SelectorGroup(s));
            }

            Map<String, String> styleAttrs = extractStyles(styles);
            CssBlock res = new CssBlock(selectors, styleAttrs);
            return res;
        }
        else {
            logger.warn("Failed to parse css block: "+cssBlock);
        }
        return null;
    }


    /**
     * Converts string
     * <pre>
     * attr1 : val1;
     * attr2 : val2;
     * </pre>
     * into the map <Name, Value>
     * @param style string with style
     * @return map
     */
    public static Map<String, String> extractStyles(String style) {

        Map<String, String> result = new HashMap<String, String>();
        if (StringUtils.isEmpty(style)) {
            return result;
        }
        String[] tokens = style.split(";");
        for (String token : tokens) {
            if (StringUtils.isNotBlank(token)) {
                String[] nameVal = token.split(":");
                String name = nameVal[0].trim();
                String val = nameVal[1].trim();
                result.put(name, val);
            }
        }
        return result;
    }

}
