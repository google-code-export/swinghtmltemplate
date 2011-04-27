package ru.swing.html.mydoggy;

import junit.framework.TestCase;
import ru.swing.html.Utils;

public class ToolWindowTest extends TestCase {

    public void testAttributes() throws Exception {
        ToolWindow toolWindow = new ToolWindow();

        //test visible
        //default to false (toolwindow is not visible)
        assertFalse((Boolean) Utils.convertStringToObject(toolWindow.getVisible(), Boolean.class));

        toolWindow.setAttribute("visible", "true");
        assertTrue((Boolean) Utils.convertStringToObject(toolWindow.getVisible(), Boolean.class));

        toolWindow.setAttribute("visible", "false");
        assertFalse((Boolean) Utils.convertStringToObject(toolWindow.getVisible(), Boolean.class));

        //test title
        toolWindow.setAttribute("title", "foo");
        assertEquals("foo", toolWindow.getTitle());

        //test icon
        toolWindow.setAttribute("icon", "foo");
        assertEquals("foo", toolWindow.getIcon());
        
    }

    public void testCreateComponent() throws Exception {
        ToolWindow toolWindow = new ToolWindow();
        assertNull(toolWindow.createComponent());
    }
}
