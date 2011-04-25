package ru.swing.html;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.*;
import org.jdesktop.observablecollections.ObservableCollections;
import ru.swing.html.css.CssBlock;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
    private Object controller;
    private Map<String, Tag> tagsById = new HashMap<String, Tag>();
    private Map<String, Object> model = ObservableCollections.observableMap(new HashMap<String, Object>());
    private String sourcePath;
    private Map<String, Map<String, Binding>> bindingsByModelElementName = new HashMap<String, Map<String, Binding>>();

    private PropertyChangeListener controllerPCL = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            String key = evt.getPropertyName();
            if (model.containsKey(key)) {
                replaceModelElement(key, evt.getNewValue());
            }
        }
    };

    private void replaceModelElement(String key, Object newValue) {
        model.put(key, newValue);
        rebindModelElement(key);
    }

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
                logger.warn("Duplicate id: "+tag.getId());
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
        SelectorGroup s = new SelectorGroup(selector);
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

    /**
     * Selects tags using selector
     * @param selector selector string (comma separated selectors)
     * @return matched tags
     */
    public Tag[] query(String selector) {
        SelectorGroup s = new SelectorGroup(selector);
        List<Tag> tags = selectTags(getRootTag(), s);
        return tags.toArray(new Tag[tags.size()]);
    }

    private List<Tag> selectTags(Tag tag, SelectorGroup selector) {
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

    /**
     * Возвращает путь до документа, из которого была загружена данная модель.
     * @return путь до документа
     */
    public String getSourcePath() {
        return sourcePath;
    }

    /**
     * Устанавливает путь до документа, из которого была загружена данная модель.
     * @param sourcePath путь до документа
     */
    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    /**
     * Returns controller this model is binded to.
     * @return controller
     */
    public Object getController() {
        return controller;
    }

    /**
     * Sets controller this model is binded to.
     * @param controller
     */
    public void setController(Object controller) {
        if (this.controller!=null) {
            if (BeanUtils.supportsBoundProperties(controller.getClass())) {
                BeanUtils.removePropertyChangeListener(controller, controllerPCL);
                logger.trace("Removed property change listener from controller "+controller.getClass());
            }
        }
        this.controller = controller;
        if (BeanUtils.supportsBoundProperties(controller.getClass())) {
            BeanUtils.addPropertyChangeListener(controller, controllerPCL);
            logger.trace("Added property change listener to controller "+controller.getClass());
        }
    }

    /**
     * Adds element to the model at the specified name.
     * @param name name of the element
     * @param element the element
     */
    public void addModelElement(String name, Object element) {
        model.put(name, element);
    }

    /**
     * Вызывает #bind() с типом синхронизации AutoBinding.UpdateStrategy.READ_WRITE
     * @param elPath описывает свойство элемента модели, которое следует синхронизировать.
     * @param component компонент, с которым следует выполнять синхронизацию.
     * @param componentProperty свойства компонента, которое требуется синхронизировать.
     * @see #bind(String, javax.swing.JComponent, org.jdesktop.beansbinding.BeanProperty, org.jdesktop.beansbinding.AutoBinding.UpdateStrategy)
     */
    public void bind(String elPath, JComponent component, Property componentProperty) {
        bind(elPath, component, componentProperty, AutoBinding.UpdateStrategy.READ_WRITE);
    }

    /**
     * <p>
     * Синхронизирует элемент модели с компонентом. Элемент должен быть предварительно помещен в модель с помощью
     * метода #addModelElement.
     * </p>
     * <p>
     *     elPath задается в формате EL, например, ${foo.name}, где
     *     <ul>
     *         <li>foo - имя элемента модели</li>
     *         <li>name - свойства объекта foo, подлежащее синхронизации</li>
     *     </ul>
     * </p>
     * @param elPath описывает свойство элемента модели, которое следует синхронизировать.
     * @param component компонент, с которым следует выполнять синхронизацию.
     * @param componentProperty свойства компонента, которое требуется синхронизировать.
     * @param type тип синхронизации
     */
    public void bind(String elPath, JComponent component, Property componentProperty, AutoBinding.UpdateStrategy type) {
        //extract model element name from elPath
        //elPath looks like ${foo.name}, where 'foo' is model element name
        String key = null;
        if (StringUtils.isNotEmpty(elPath) && elPath.startsWith("${")) {
            int closingPos = elPath.indexOf("}", 2);
            String el = elPath.substring(2, closingPos);
            int dotIndex = el.indexOf('.');
            if (dotIndex<0) {
                dotIndex = el.length();
            }
            key = el.substring(0, dotIndex);
            if (bindingsByModelElementName.get(key)!=null) {
                Map<String, Binding> map = bindingsByModelElementName.get(key);
                if (map.get(elPath)!=null) {
                    map.get(elPath).unbind();
                    logger.debug("Unbinded model element: "+map.get(elPath).toString());
                }
            }

        }

        ELProperty<Map<String, Object>, String> beanProperty = ELProperty.create(elPath);
        Binding binding = Bindings.createAutoBinding(type, model, beanProperty, component, componentProperty);
        binding.bind();
        logger.debug("Binded '"+elPath+"'");

        if (key!=null) {
            Map<String, Binding> bindings = bindingsByModelElementName.get(key);
            if (bindings==null) {
                bindings = new HashMap<String, Binding>();
                bindingsByModelElementName.put(key, bindings);
            }
            bindings.put(elPath, binding);
        }
    }


    public void rebindModelElement(String key) {
        logger.trace("Rebinding all bindings of '"+key+"' model element.");
        if (bindingsByModelElementName.containsKey(key)) {
            Map<String, Binding> bindings = bindingsByModelElementName.get(key);
            bindingsByModelElementName.remove(key);
            //unbind old bindings and bean new ones
            for (String elPath : bindings.keySet()) {
                Binding b = bindings.get(elPath);
                b.unbind();
                logger.debug("Unbinded binding: " + elPath);

                bind(elPath, (JComponent) b.getTargetObject(), (BeanProperty) b.getTargetProperty());
            }
        }
    }


    public Map<String, Object> getModelElements() {
        return model;
    }


    /**
     * Removes source tag from model. Every direct child of target's tag will be inserted into source's parent tag.
     *
     * Do nothing if source is root tag (source.getParent() returns null).
     *
     * @param sourceTag tag to remove
     * @param targetTag tag, whose children will be inserted
     */
    public void mergeTag(Tag sourceTag, Tag targetTag) {
        List<Tag> targetChildren = targetTag.getChildren();
        Tag parent = sourceTag.getParent();
        if (targetChildren !=null && parent !=null) {

            parent.removeChild(sourceTag);

            for (Tag bodyChild : targetChildren) {
                parent.addChild(bodyChild);
            }
        }

    }

    /**
     * Clears all id-tag mappings.
     */
    public void resetIds() {
        tagsById.clear();
    }
}
