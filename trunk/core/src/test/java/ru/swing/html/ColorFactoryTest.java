package ru.swing.html;

import junit.framework.TestCase;

import java.awt.*;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 23.11.2010
 * Time: 11:13:40
 * </pre>
 */
public class ColorFactoryTest extends TestCase {

    public void testGetColor() throws Exception {
        assertEquals(Color.white, ColorFactory.getColor("white"));
        assertEquals(Color.black, ColorFactory.getColor("black"));
        assertEquals(Color.blue, ColorFactory.getColor("blue"));
        assertEquals(Color.red, ColorFactory.getColor("red"));
        assertEquals(Color.green, ColorFactory.getColor("lime"));

        assertEquals(Color.white, ColorFactory.getColor("#ffffff"));
        assertEquals(Color.black, ColorFactory.getColor("#000000"));
        assertEquals(Color.blue, ColorFactory.getColor("#0000ff"));
        assertEquals(Color.red, ColorFactory.getColor("#ff0000"));
        assertEquals(Color.green, ColorFactory.getColor("#00ff00"));

    }

    public void testParseHexColor() throws Exception {
        assertEquals(Color.white, ColorFactory.parseHexColor("#ffffff"));
        assertEquals(Color.black, ColorFactory.parseHexColor("#000000"));
        assertEquals(Color.blue, ColorFactory.parseHexColor("#0000ff"));
        assertEquals(Color.red, ColorFactory.parseHexColor("#ff0000"));
        assertEquals(Color.green, ColorFactory.parseHexColor("#00ff00"));
    }
}
