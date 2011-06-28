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

    public void testConvertStringToClass() throws Exception {

        assertEquals(Boolean.TYPE, Utils.convertStringToClass("boolean"));
        assertEquals(Byte.TYPE, Utils.convertStringToClass("byte"));
        assertEquals(Short.TYPE, Utils.convertStringToClass("short"));
        assertEquals(Integer.TYPE, Utils.convertStringToClass("int"));
        assertEquals(Character.TYPE, Utils.convertStringToClass("char"));
        assertEquals(Long.TYPE, Utils.convertStringToClass("long"));
        assertEquals(Float.TYPE, Utils.convertStringToClass("float"));
        assertEquals(Double.TYPE, Utils.convertStringToClass("double"));
        assertEquals(String.class, Utils.convertStringToClass("java.lang.String"));

    }

    public void testConvertStringToObject() {

        assertEquals("foo", Utils.convertStringToObject("foo", String.class));

        assertEquals(Boolean.TRUE, Utils.convertStringToObject("true", Boolean.class));
        assertEquals(Boolean.FALSE, Utils.convertStringToObject("false", Boolean.class));
        assertEquals(Boolean.TRUE, Utils.convertStringToObject("true", Boolean.TYPE));
        assertEquals(Boolean.FALSE, Utils.convertStringToObject("false", Boolean.TYPE));

        assertEquals((Integer)1, Utils.convertStringToObject("1", Integer.class));
        assertEquals((Integer)1, Utils.convertStringToObject("1", Integer.TYPE));

        assertEquals(2.1f, Utils.convertStringToObject("2.1", Float.class));
        assertEquals(2.1f, Utils.convertStringToObject("2.1", Float.TYPE));

        assertEquals(1.01d, Utils.convertStringToObject("1.01", Double.class));
        assertEquals(1.01d, Utils.convertStringToObject("1.01", Double.TYPE));
        
        assertEquals((Long)50L, Utils.convertStringToObject("50", Long.class));
        assertEquals((Long)50L, Utils.convertStringToObject("50", Long.TYPE));

        assertEquals(Short.valueOf((short) 50), Utils.convertStringToObject("50", Short.class));
        assertEquals(Short.valueOf((short) 50), Utils.convertStringToObject("50", Short.TYPE));

        assertEquals(Byte.valueOf((byte) 50), Utils.convertStringToObject("50", Byte.class));
        assertEquals(Byte.valueOf((byte) 50), Utils.convertStringToObject("50", Byte.TYPE));

        assertEquals(Character.valueOf('f'), Utils.convertStringToObject("f", Character.class));
        assertEquals(Character.valueOf('f'), Utils.convertStringToObject("f", Character.TYPE));

        assertEquals(new Dimension(1, 2), Utils.convertStringToObject("1 2", Dimension.class));
        assertEquals(new Insets(1, 2, 3, 4), Utils.convertStringToObject("1 2 3 4", Insets.class));
        assertEquals(new Point(1, 2), Utils.convertStringToObject("1 2", Point.class));
        assertEquals(new Rectangle(1, 2, 3, 4), Utils.convertStringToObject("1 2 3 4", Rectangle.class));

    }


    public void testFindActionMethod() throws Exception {
        assertNotNull(Utils.findActionMethod(Foo.class, "foo", null));
        assertNotNull(Utils.findActionMethod(Foo1.class, "foo", Integer.class));
        assertNull(Utils.findActionMethod(Foo1.class, "foo1", Integer.class));
    }


    public void testUnwrap() throws Exception {
        assertEquals("foo", Utils.unwrap("'foo'"));
        assertEquals("foo", Utils.unwrap("\"foo\""));
        assertEquals("\"foo'", Utils.unwrap("\"foo'"));
        assertEquals("foo", Utils.unwrap("foo"));
        assertEquals("", Utils.unwrap("''"));
        assertEquals("", Utils.unwrap("\"\""));
        assertEquals("\"", Utils.unwrap("\""));
        assertEquals("'", Utils.unwrap("'"));
    }

    /**
     * Test class for testing Utils.findActionMethod().
     */
    public class Foo {

        public void foo() {}

    }

    /**
     * Test class for testing Utils.findActionMethod().
     */
    public class Foo1 {

        public void foo(Integer i) {}

    }

}
