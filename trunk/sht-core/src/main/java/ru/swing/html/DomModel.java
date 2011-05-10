package ru.swing.html;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.*;
import org.jdesktop.observablecollections.ObservableCollections;
import ru.swing.html.configuration.Configuration;
import ru.swing.html.configuration.DefaultConfiguration;
import ru.swing.html.css.CssBlock;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * dom-model. Holds the tags tree (starting with root tag), css styles etc
 */
public class DomModel {

    private int tagCounter = 0;
    private Log logger = LogFactory.getLog(getClass());
    private List<CssBlock> globalStyles = new ArrayList<CssBlock>();
    private Tag rootTag;
    private Object controller;
    private Map<String, Tag> tagsById = new HashMap<String, Tag>();
    private Map<String, Object> model = ObservableCollections.observableMap(new HashMap<String, Object>());
    private String sourcePath;
    private Window window;
    private Map<String, Map<String, Binding>> bindingsByModelElementName = new HashMap<String, Map<String, Binding>>();
    private Configuration configuration = new DefaultConfiguration();
    /**
     * Contains meta data, filled by <meta> tags inside <head>
     */
    private Map<String, String> metaItems = new HashMap<String, String>();

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
     * Returns the root tag of the model. The root tag is usual &lt;html%gt; tag.
     * @return the root tag
     */
    public Tag getRootTag() {
        return rootTag;
    }

    public void setRootTag(Tag rootTag) {
        this.rootTag = rootTag;
    }

    /**
     * <p>
     * Returns tag by it's id. If no tag is found, returns null.
     * </p>
     * <p>
     *     Tag's id is set with "id" attribute.
     * </p>
     * @param id tag's id
     * @return tag with supplied id or null
     */
    public Tag getTagById(String id) {
        return tagsById.get(id);
    }

    /**
     * For each tag extract tag ids and store them in the map
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
     * Selects component with selector.
     * @param selector selector
     * @return components, whose tags match selector.
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
     * Selects tags using selector within root tag.
     * @param selector selector string (comma separated selectors)
     * @return matched tags, wrapped with QueryResult
     */
    public QueryResult query(String selector) {
        return query(selector, getRootTag());
    }

    /**
     * Selects tags using selector within specified tag.
     * @param selector selector string (comma separated selectors)
     * @param context the tag, searching is done within it
     * @return matched tags, wrapped with QueryResult
     */
    public QueryResult query(String selector, Tag context) {
        SelectorGroup s = new SelectorGroup(selector);
        List<Tag> tags = selectTags(context, s);
        return new QueryResult(tags.toArray(new Tag[tags.size()]));
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
     * Adds css style to the mode. Style doesn't being applied to the tags.
     * @param cssBlock css style
     */
    public void addGlobalStyle(CssBlock cssBlock) {
        globalStyles.add(cssBlock);
    }

    /**
     * Returns collection of css styles for model.
     * @return collection of css styles
     */
    public List<CssBlock> getGlobalStyles() {
        return globalStyles;
    }

    /**
     * Returns the document path for the model. This is used when parsing relative paths in urls.
     * @return document path
     * @see ru.swing.html.configuration.ResourceLoader#loadResource(DomModel, String)
     */
    public String getSourcePath() {
        return sourcePath;
    }

    /**
     * Sets the document path for the model. This is used when parsing relative paths in urls.
     * @param sourcePath document path
     * @see ru.swing.html.configuration.ResourceLoader#loadResource(DomModel, String)
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
        if (controller!=null) {
            if (BeanUtils.supportsBoundProperties(controller.getClass())) {
                BeanUtils.addPropertyChangeListener(controller, controllerPCL);
                logger.trace("Added property change listener to controller "+controller.getClass());
            }
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
     * Calls #bind() this AutoBinding.UpdateStrategy.READ_WRITE as binding type
     * @param elPath el to model element property, with which the binding will be created
     * @param component component to bind
     * @param componentProperty component's property to bind
     * @see #bind(String, javax.swing.JComponent, org.jdesktop.beansbinding.Property, org.jdesktop.beansbinding.AutoBinding.UpdateStrategy)
     */
    public void bind(String elPath, JComponent component, Property componentProperty) {
        bind(elPath, component, componentProperty, AutoBinding.UpdateStrategy.READ_WRITE);
    }

    /**
     * <p>
     *     Binds model element with component. Element must be already contained in the model (use #addModelElement).
     * </p>
     * <p>
     *     elPath is set in the format of EL, e.g., ${foo.name}, where
     *     <ul>
     *         <li>foo - the name of the model element</li>
     *         <li>name - foo's property, to be binded</li>
     *     </ul>
     * </p>
     * @param elPath el to the model's property ti bind
     * @param component component to bind
     * @param componentProperty component's property to bind
     * @param type binding type
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

                bind(elPath, (JComponent) b.getTargetObject(), b.getTargetProperty());
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

            int index = parent.getChildren().indexOf(sourceTag);
            parent.removeChild(sourceTag);

            for (Tag bodyChild : targetChildren) {
                parent.addChild(bodyChild, index++);
            }
        }

        DomConverter.recursivellyVisitTags(parent, new TagVisitor() {
            public void visit(Tag tag) {
                tag.setModel(DomModel.this);
            }
        });
    }

    /**
     * Clears all id-tag mappings.
     */
    public void resetIds() {
        tagsById.clear();
    }

    public Map<String, String> getMetaItems() {
        return metaItems;
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public int nextTagCount() {
        return tagCounter++;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public String dump() {
        return dump(getRootTag(), 0);
    }

    private String dump(Tag tag, int pad) {
        final StringBuilder sb = new StringBuilder();
        String padStr = StringUtils.repeat(" ", pad * 3);
        sb.append(padStr);
        sb.append("<").append(tag.getName()).append(" id='").append(tag.getId()).append("'");

        for (String attrName : tag.getAttributes().keySet()) {
            sb.append(" ").append(attrName).append("='").append(tag.getAttribute(attrName)).append("'");
        }

        if (StringUtils.isEmpty(tag.getContent()) && tag.getChildren().isEmpty()) {
            sb.append("/>\n");
        }
        else {
            sb.append(">\n");

            if (StringUtils.isNotEmpty(tag.getContent())) {
                sb.append(tag.getContent());
            }

            for (Tag child : tag.getChildren()) {
                sb.append(dump(child, pad+1));
            }
            sb.append(padStr).append("</").append(tag.getName()).append(">\n");
        }
        return sb.toString();

    }

}
