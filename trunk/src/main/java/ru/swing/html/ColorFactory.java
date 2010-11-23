package ru.swing.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Вспомогательный класс для работы с цветами.
 * <a href="http://www.w3.org/TR/CSS21/syndata.html#color-units">Здесь</a> располагается карта предопределенных
 * цветов, к которым можно образаться по имени. Также цвет можно получить по его hex-представлению: #ffffff.
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
     * Возвращает цвет по его имени.
     * Если название цвета содержится в предустановленной палитре, используется соответствующее hex представление.
     * Если цвет начинается с символа '#', то имя цвета используется как hex представление.
     * В противном случае ищется в системных свойствах свойство с указанным именем, и его значение используется как
     * числовое представление цвета.
     * @param name название цвета
     * @return цвет
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
     * Разбирает hex представление цвета и возвращает объект этого цвета.
     * @param hex представление цвета в формате web, например, #112233
     * @return цвет или null, если произошла ошибка определения цвета
     */
    public static Color parseHexColor(String hex) {
        if (hex.startsWith("#")) {
            return Color.decode("0x"+hex.substring(1));
        }
        return null;
    }
}
