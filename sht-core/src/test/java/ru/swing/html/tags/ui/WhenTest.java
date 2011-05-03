package ru.swing.html.tags.ui;

import junit.framework.TestCase;

public class WhenTest extends TestCase {

    public void testTagAttributes() throws Exception {
        When when = new When();
        when.setAttribute("test", "${someExpr}");
        assertEquals("${someExpr}", when.getTest());
    }
}
