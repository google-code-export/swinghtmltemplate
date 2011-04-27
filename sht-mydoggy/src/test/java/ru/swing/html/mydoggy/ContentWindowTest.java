package ru.swing.html.mydoggy;

import junit.framework.TestCase;
import ru.swing.html.Utils;

public class ContentWindowTest extends TestCase {

    public void testAttributes() throws Exception {
        ContentWindow contentWindow = new ContentWindow();

        //test title
        contentWindow.setAttribute("title", "foo");
        assertEquals("foo", contentWindow.getTitle());

        //test icon
        contentWindow.setAttribute("icon", "foo");
        assertEquals("foo", contentWindow.getIcon());
        
    }

    public void testCreateComponent() throws Exception {
        ContentWindow contentWindow = new ContentWindow();
        assertNull(contentWindow.createComponent());
    }
}
