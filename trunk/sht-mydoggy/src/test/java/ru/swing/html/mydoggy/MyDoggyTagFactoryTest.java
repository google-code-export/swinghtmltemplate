package ru.swing.html.mydoggy;

import junit.framework.TestCase;
import org.jdom.Element;

public class MyDoggyTagFactoryTest extends TestCase {

    public void testTagCreation() throws Exception {

        MyDoggyTagFactory factory = new MyDoggyTagFactory();

        assertEquals(ContentWindow.class, factory.createTag(new Element("contentWindow")).getClass());
        assertEquals(MainWindow.class, factory.createTag(new Element("mainWindow")).getClass());
        assertEquals(ToolWindow.class, factory.createTag(new Element("toolWindow")).getClass());
    }
}
