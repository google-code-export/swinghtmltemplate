package ru.swing.html;

import org.jdom.Element;
import ru.swing.html.tags.Tag;

/**
 * TagFactory
 */
public interface TagFactory {

    /**
     * Фабрика тегов. Создает тег по имени jdom элемента.
     * @param element jdom-элемент
     * @return тег
     */
    public Tag createTag(Element element);

}
