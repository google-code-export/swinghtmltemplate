package ru.swing.html.xhtmlrenderer;

import junit.framework.TestCase;

public class XhtmlRendererTest extends TestCase {

    public void testCreateTag() throws Exception {
        assertNotNull(new XhtmlRendererFactory().createTag("xhtmlpanel"));
        assertEquals(XhtmlRenderer.class, new XhtmlRendererFactory().createTag("xhtmlpanel").getClass());
    }
}
