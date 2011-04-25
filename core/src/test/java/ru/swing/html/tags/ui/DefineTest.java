package ru.swing.html.tags.ui;

import junit.framework.TestCase;

/**
 * Test &lt;define> tag
 */
public class DefineTest extends TestCase {

    public void testAttributes() throws Exception {
        
        Define define = new Define();
        String value = "foo";
        define.setAttribute("name", value);
        assertEquals(value, define.getSnippetName());

    }
}
