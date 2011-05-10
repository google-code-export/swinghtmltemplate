package ru.swing.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Additional class for the work with colors.
 * <a href="http://www.w3.org/TR/CSS21/syndata.html#color-units">Here</a> you can see predefined colors
 * which you can use by name. You can also get the color by it's hex value: #ffffff.
 *
 * @see #getColor(String)
 */
public class ColorFactory {

    private static Log logger = LogFactory.getLog(ColorFactory.class);
    private static Map<String, String> colorMap = new HashMap<String, String>();
    static {
        Properties p = new Properties();
        try {
            p.load(ColorFactory.class.getResourceAsStream("/ru/swing/html/colors.properties"));
        } catch (IOException e) {
            logger.error("Failed to load colors map: "+e.getMessage());
        }

        for (Object key : p.keySet()) {
            String colorName = (String) key;
            String colorHex = p.getProperty(colorName);
            colorMap.put(colorName, colorHex);
            logger.trace("Loaded color: " + colorName + "->" + colorHex);
        }
    }

    /**
     * Gets the color by name.
     * If the name is contained in predefined colors, the corresponding hex value is used.
     * If name starts with "#", then the name is used as hex value.
     * Otherwise the name is searched within system properties and it's value is used as the integer for
     * the color value.
     * @param name the name of the color
     * @return color
     * @see #parseHexColor(String)
     * @see Color#getColor(String)
     */
    public static Color getColor(String name) {
        if (colorMap.containsKey(name)) {
            return parseHexColor(colorMap.get(name));
        }
        else if (name.startsWith("#")) {
            return parseHexColor(name);
        }
        else {
            return Color.getColor(name);
        }
    }


    /**
     * Parses hex value of the color and returns the color for it.
     * @param hex hex-value in web format, e.g., #112233
     * @return color or null, if color can't be parsed
     */
    public static Color parseHexColor(String hex) {
        if (hex.startsWith("#")) {
            return Color.decode("0x"+hex.substring(1));
        }
        return null;
    }
}
