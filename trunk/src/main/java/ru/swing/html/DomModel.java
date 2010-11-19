package ru.swing.html;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.tags.Tag;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 10:49:18
 * </pre>
 */
public class DomModel {

    private Log logger = LogFactory.getLog(getClass());
    private Tag rootTag;
    private Map<String, Tag> tagsById = new HashMap<String, Tag>();

    public Tag getRootTag() {
        return rootTag;
    }

    public void setRootTag(Tag rootTag) {
        this.rootTag = rootTag;
    }

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
}
