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
     * Разбивает строку css стилей на блоки. Каждый блок соответствует 1 стилю.
     * <pre>
     * .foo1 { nam1: val1} foo2 { nam2: val2 }
     * </pre>
     * будет разбит на 2 блока
     * @param css строка стилей
     * @return список блоков стилей
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
     * Извлекает информацию о стилях из css блока.
     * @param cssBlock строка css блока
     * @return информация о css блоке или null, если не удалось извлечь информацию
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
