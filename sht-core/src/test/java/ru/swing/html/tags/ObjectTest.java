package ru.swing.html.tags;

import junit.framework.TestCase;
import ru.swing.html.DomConverter;

import javax.swing.*;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 24.11.2010
 * Time: 11:26:43
 * </pre>
 */
public class ObjectTest extends TestCase {

    public void testCreateComponent() throws Exception {

        Object tag = new Object();
        tag.setAttribute("classid", JButton.class.getName());
        assertEquals(JButton.class, DomConverter.convertComponent(tag).getClass());

    }
}
