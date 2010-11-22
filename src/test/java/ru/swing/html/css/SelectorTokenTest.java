package ru.swing.html.css;

import junit.framework.TestCase;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 22.11.2010
 * Time: 12:51:30
 * </pre>
 */
public class SelectorTokenTest extends TestCase {

    public void testSelectorToken() throws Exception {
        SelectorToken token = new SelectorToken("p#id.class1.class2");
        assertEquals("p", token.getName());
        assertEquals("id", token.getId());
        assertEquals(2, token.getClasses().length);

        token = new SelectorToken(".class1.class2");
        assertNull(token.getName());
        assertNull(token.getId());
        assertEquals(2, token.getClasses().length);


    }
}
