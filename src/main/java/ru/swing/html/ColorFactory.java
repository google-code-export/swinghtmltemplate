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

    public static final Color getColor(String name) {
        if ("red".equals(name)) {
            return Color.red;
        }
        return null;
    }

}
