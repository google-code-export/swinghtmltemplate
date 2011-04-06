package ru.swing.html.tags;

import junit.framework.TestCase;
import net.miginfocom.swing.MigLayout;
import ru.swing.html.DomConverter;

import javax.swing.*;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 24.11.2010
 * Time: 11:00:25
 * </pre>
 */
public class ImgTest extends TestCase {

    public void testConvertsToJLabel() throws Exception {

        Img tag = new Img();
        JComponent jComponent = DomConverter.convertComponent(tag);
        assertNotNull(jComponent);
        assertEquals(JLabel.class, jComponent.getClass());


    }


    public void testSrcAttribute() throws Exception {

        Img tag = new Img();
        tag.setAttribute("src", "/foo.png");
        tag.applyAttributes(null);
        assertEquals("/foo.png", tag.getAttribute("icon"));


    }


}
