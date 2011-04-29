package ru.swing.html;

import org.jdom.Element;
import ru.swing.html.tags.Tag;

/**
 * TagFactory
 */
public interface TagFactory {

    /**
     * Фабрика тегов. Создает тег по имени тега.
     * @param name имя тега
     * @return тег
     */
    public Tag createTag(String name);

}
