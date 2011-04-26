package ru.swing.html.mydoggy;

import org.jdom.Element;
import ru.swing.html.TagFactory;
import ru.swing.html.tags.Tag;

public class MyDoggyTagFactory implements TagFactory {


    public Tag createTag(Element element) {
        if ("mainWindow".equals(element.getName())) {
            return new MainWindow();
        }
        else if ("toolWindow".equals(element.getName())) {
            return new ToolWindow();
        }
        else if ("contentWindow".equals(element.getName())) {
            return new ContentWindow();
        }
        return null;
    }
}
