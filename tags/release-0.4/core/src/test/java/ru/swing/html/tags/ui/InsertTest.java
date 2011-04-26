package ru.swing.html.tags.ui;

import junit.framework.TestCase;

/**
 * Test &lt;define> tag
 */
public class InsertTest extends TestCase {

    public void testAttributes() throws Exception {
        
        Insert insert = new Insert();
        String value = "foo";
        insert.setAttribute("name", value);
        assertEquals(value, insert.getSnippetName());

    }

}
