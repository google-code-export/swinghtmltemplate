package ru.swing.html;

import java.awt.*;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 18:35:18
 * </pre>
 */
public class ColorFactory {

    public static Color getColor(String name) {
        if ("red".equals(name)) {
            return Color.red;
        }
        else if ("blue".equals(name)) {
            return Color.blue;
        }
        else if ("black".equals(name)) {
            return Color.black;
        }
        else if ("yellow".equals(name)) {
            return Color.yellow;
        }
        else if ("pink".equals(name)) {
            return Color.pink;
        }
        else if ("white".equals(name)) {
            return Color.white;
        }
        else if ("green".equals(name)) {
            return Color.green;
        }
        return null;
    }

}
