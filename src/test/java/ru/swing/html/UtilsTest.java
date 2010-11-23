package ru.swing.html;

import junit.framework.TestCase;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 23.11.2010
 * Time: 15:06:02
 * </pre>
 */
public class UtilsTest extends TestCase {

    public void testExtractParams() throws Exception {

        String[] params = Utils.extractParams("  \"qq1\"  qq2;\tqq3 ");
        assertEquals(3, params.length);
        assertEquals("\"qq1\"", params[0]);
        assertEquals("qq2;", params[1]);
        assertEquals("qq3", params[2]);

    }

    public void testMergeSpaces() throws Exception {
        assertEquals("qq qq", Utils.mergeSpaces("qq  qq"));
        assertEquals("qq qq", Utils.mergeSpaces("  qq     qq   "));
    }

    public void testFindMatchingClosingBracket() throws Exception {
        assertEquals(1, Utils.fingMatchingClosingBracket("()", 0));
        assertEquals(2, Utils.fingMatchingClosingBracket("(())", 1));
        assertEquals(3, Utils.fingMatchingClosingBracket("(())", 0));
        assertEquals(5, Utils.fingMatchingClosingBracket("(()())", 0));
    }
}
