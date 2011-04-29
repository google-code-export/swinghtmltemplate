package ru.swing.html.mydoggy;

import org.jdom.Element;
import ru.swing.html.TagFactory;
import ru.swing.html.tags.Tag;

public class MyDoggyTagFactory implements TagFactory {


    public Tag createTag(String name) {
        if ("mainWindow".equals(name)) {
            return new MainWindow();
        }
        else if ("toolWindow".equals(name)) {
            return new ToolWindow();
        }
        else if ("contentWindow".equals(name)) {
            return new ContentWindow();
        }
        return null;
    }
}
