package ru.swing.html.configuration;

import junit.framework.TestCase;

public class ClasspathResourceLoaderTests extends TestCase {

    public void testLoadResource() throws Exception {

        ClasspathResourceLoader loader = new ClasspathResourceLoader();
        assertNotNull(loader.loadResource(null, "img/accept.png"));
        assertNotNull(loader.loadResource(null, "/img/accept.png"));

    }
}
