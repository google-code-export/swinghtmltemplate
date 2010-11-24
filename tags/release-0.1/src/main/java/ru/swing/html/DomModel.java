package ru.swing.html;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.css.CssBlock;
import ru.swing.html.css.Selector;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * dom-модель компонент. Хранит в себе дерево тегов (начиная с корневого элемента getRootTag()), список css стилей
 * (getGlobalStyles()).
 *
 * css стили - это стили, описанные в теге &lt;style&gt; внутри тега &lt;head&gt;. 
 */
public class DomModel {

    private Log logger = LogFactory.getLog(getClass());
    private List<CssBlock> globalStyles = new ArrayList<CssBlock>();
    private Tag rootTag;
    private Map<String, Tag> tagsById = new HashMap<String, Tag>();

    /**
     * Возвращает корневой элемент модели. Корневой элемент соответствует тегу &lt;html%gt;.
     * @return корневой элемент модели
     */
    public Tag getRootTag() {
        return rootTag;
    }

    public void setRootTag(Tag rootTag) {
        this.rootTag = rootTag;
    }

    /**
     * Возвращает тег по его идентификатору. Если такой тег не найден, возвращает null.
     * Идентификатор тега задается в html документе с помощью атрибута id.
     * @param id идентфикатор тега
     * @return тег
     */
    public Tag getTagById(String id) {
        return tagsById.get(id);
    }

    /**
     * Проходит по дереву тегов и составляет карту <Id, Тэг>
     */
    public void fillIds() {
        fillIdsWithinTag(rootTag);
    }

    private void fillIdsWithinTag(Tag tag) {
        if (StringUtils.isNotEmpty(tag.getId())) {
            if (tagsById.containsKey(tag.getId())) {
                logger.warn("Duplicapte id: "+tag.getId());
            }
            else {
                tagsById.put(tag.getId(), tag);
            }
        }

        for (Tag child : tag.getChildren()) {
            fillIdsWithinTag(child);
        }
    }

    /**
     * Выбирает компоненты по селектору.
     * @param selector строка селектора
     * @return компоненты, теги которых удовлетворяют селектору.
     */
    public JComponent[] select(String selector) {
        Selector s = new Selector(selector);
        List<Tag> tags = selectTags(getRootTag(), s);
        List<JComponent> components = new ArrayList<JComponent>(tags.size());
        for (Tag tag : tags) {
            JComponent c = tag.getComponent();
            if (c!=null) {
                components.add(c);
            }
        }
        return components.toArray(new JComponent[components.size()]);
    }

    private List<Tag> selectTags(Tag tag, Selector selector) {
        List<Tag> res = new ArrayList<Tag>();
        if (selector.matches(tag)) {
            res.add(tag);
        }
        for (Tag child : tag.getChildren()) {
            res.addAll(selectTags(child, selector));
        }
        return res;
    }


    /**
     * Добавляет css стиль в модель. Стиль при этом не применяется к имеющимся в модели тегам.
     * @param cssBlock css стиль
     */
    public void addGlobalStyle(CssBlock cssBlock) {
        globalStyles.add(cssBlock);
    }

    /**
     * Возвращает список css стилей.
     * @return список css стилей
     */
    public List<CssBlock> getGlobalStyles() {
        return globalStyles;
    }

}
