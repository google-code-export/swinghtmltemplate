package ru.swing.html.css;

import junit.framework.TestCase;
import ru.swing.html.tags.Tag;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 22.11.2010
 * Time: 13:03:26
 * </pre>
 */
public class SelectorTest extends TestCase {

    public void testMatches() throws Exception {

        Selector selector = new Selector("p");
        Tag tag = new Tag();
        tag.setName("p");
        assertTrue(selector.matches(tag));

        selector = new Selector(".foo");
        tag = new Tag();
        tag.setName("p");
        tag.setAttribute("class", "foo");
        assertTrue(selector.matches(tag));

        selector = new Selector("#foo");
        tag = new Tag();
        tag.setId("foo");
        assertTrue(selector.matches(tag));

        selector = new Selector("p.foo");
        tag = new Tag();
        tag.setName("p");
        tag.setAttribute("class", "foo bar");
        assertTrue(selector.matches(tag));

        selector = new Selector("p.foo.bar");
        tag = new Tag();
        tag.setName("p");
        tag.setAttribute("class", "foo bar");
        assertTrue(selector.matches(tag));
        tag.setAttribute("class", "foo");
        assertFalse(selector.matches(tag));

        selector = new Selector("*");
        tag = new Tag();
        tag.setName("p");
        tag.setAttribute("class", "foo bar");
        assertTrue(selector.matches(tag));

    }
}
