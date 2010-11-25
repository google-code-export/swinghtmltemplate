package ru.swing.html;

import junit.framework.TestCase;

import java.awt.*;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 23.11.2010
 * Time: 15:06:02
 * </pre>
 */
public class UtilsTest extends TestCase {

    public void testParseIntegers() throws Exception {

        String numbers = "12 13   14 3245";
        int[] nums = Utils.parseIntegers(numbers);
        assertEquals(4, nums.length);
        assertEquals(12, nums[0]);
        assertEquals(13, nums[1]);
        assertEquals(14, nums[2]);
        assertEquals(3245, nums[3]);


    }

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


    public void testConvertStringToObject() {

        assertEquals("foo", Utils.convertStringToObject("foo", String.class));
        assertEquals(1, Utils.convertStringToObject("1", Integer.class));
        assertEquals(2.1f, Utils.convertStringToObject("2.1", Float.class));
        assertEquals(1.01d, Utils.convertStringToObject("1.01", Double.class));
        assertEquals(50L, Utils.convertStringToObject("50", Long.class));
        assertEquals(new Dimension(1, 2), Utils.convertStringToObject("1 2", Dimension.class));
        assertEquals(new Insets(1, 2, 3, 4), Utils.convertStringToObject("1 2 3 4", Insets.class));

    }
}
