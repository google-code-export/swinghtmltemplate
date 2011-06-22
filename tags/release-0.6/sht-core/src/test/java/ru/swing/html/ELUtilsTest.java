package ru.swing.html;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ELUtilsTest extends TestCase {

    public void testResolve() throws Exception {

        String text = "Hello ${name}";
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("name", "world");
        String res = ELUtils.parseStringValue(text, model, new HashSet());
        assertEquals("Hello world", res);

    }

    public void testResolve2() throws Exception {

        String text = "Hello ${name}";
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("name", "world");
        model.put("link", "world");
        String res = ELUtils.parseStringValue(text, model, new HashSet());
        assertEquals("Hello world", res);

    }
}


