package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.*;
import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.AbstractLayerUI;
import org.jdesktop.jxlayer.plaf.LayerUI;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableMap;
import org.jdesktop.observablecollections.ObservableMapListener;
import ru.swing.html.*;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.css.StyleParser;
import ru.swing.html.layout.LayoutManagerSupport;
import ru.swing.html.layout.LayoutManagerSupportFactory;
import ru.swing.html.tags.event.ClickDelegator;
import ru.swing.html.tags.event.DocumentDelegator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.lang.*;
import java.lang.Object;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.AttributedCharacterIterator;
import java.util.*;
import java.util.List;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 10:47:28
 * </pre>
 */
public class Tag implements Cloneable {

    private DomModel model;
    private JComponent component;
    private JComponent componentWrapper;
    private Log logger = LogFactory.getLog(Tag.class);
    private String id;
    private String name;
    private String namespace;
    private Tag parent;
    private List<Tag> children = new ArrayList<Tag>();
    private String content;
    private String display;
    private Color color;
    private Color backgroundColor;
    /**
     * Constraint для размещения компонента в родительском компоненте.
     */
    private String align;
    private String type;
    private String textAlign;
    private String verticalAlign;
    private String margin;
    private String width;
    private String height;
    private String fontSize;
    private String fontWeight;
    private String fontStyle;
    private String fontFamily;
    private Map<String, String> attributes = new HashMap<String, String>();
    public static final String BORDER_ATTRIBUTE = "border";
    public static final String ALIGN_ATTRIBUTE = "align";
    public static final String TYPE_ATTRIBUTE = "type";
    private ClickDelegator clickDelegator;
    private DocumentDelegator documentDelegator;
    private Map<String, Object> modelElements;
    private ObservableMapListener parentMapListener;
    private ObservableMap parentMap;

    /**
     * Возвращает первый дочерний тег с указанным именем.
     * @param name имя тега (p, body, div)
     * @return тег или null, если тег не найден
     */
    public Tag getChildByName(String name) {
        for (Tag child : getChildren()) {
            if (name.equals(child.getName())) {
                return child;
            }
        }
        return null;
    }

    

    public String getAttribute(String name) {
        return attributes.get(name);
    }


    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttribute(String name, String value) {
        if ("display".equals(name)) {
            setDisplay(value);
        }
        else if ("id".equals(name)) {
            setId(value);
        }
        else if (ALIGN_ATTRIBUTE.equals(name)) {
            setAlign(value);
        }
        else if ("text-align".equals(name)) {
            setTextAlign(value);
        }
        else if (TYPE_ATTRIBUTE.equals(name)) {
            setType(value);
        }
        else if ("vertical-align".equals(name)) {
            setVerticalAlign(value);
        }
        else if ("margin".equals(name)) {
            setMargin(value);
        }
        else if ("color".equals(name)) {
            setColor(ColorFactory.getColor(value));
        }
        else if ("background-color".equals(name)) {
            setBackgroundColor(ColorFactory.getColor(value));
        }
        else if ("width".equals(name)) {
            setWidth(value);
        }
        else if ("font-size".equals(name)) {
            setFontSize(value);
        }
        else if ("font-weight".equals(name)) {
            setFontWeight(value);
        }
        else if ("font-style".equals(name)) {
            setFontStyle(value);
        }
        else if ("font-family".equals(name)) {
            setFontFamily(value);
        }
        else if ("height".equals(name)) {
            setHeight(value);
        }
        else if ("style".equals(name)) {
            Map<String, String> styles = StyleParser.extractStyles(value);
            for (String styleName : styles.keySet()) {
                setAttribute(styleName, styles.get(styleName));
            }
        }
        attributes.put(name, value);
    }

    public void handleLayout() {
        final String layoutName = getDisplay();
        LayoutManagerSupport layoutManagerSupport = null;
        if (layoutName != null && component!=null) {
            layoutManagerSupport = LayoutManagerSupportFactory.createLayout(this);
            component.setLayout(layoutManagerSupport.createLayout(this));
        }

    }

    /**
     * Tag must handle all it's children in this method.
     * Handle means:
     * 1. create child
     * 2. place child using layout manager
     * @param substitutions substitutions map for domModel
     */
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {
        //если для тега нет компонента (например, это неизвестный тег), то найдем первый родительский
        //тег с непустым компонентом и будет дочерние компоненты добавлять в него
        JComponent c = getComponent();
        Tag parent = this;
        while (c==null && parent!=null) {
            parent = parent.getParent();
            if (parent!=null) {
                c = parent.getComponent();
            }
        }

        for (Tag childTag : getChildren()) {
            JComponent child = DomConverter.convertComponent(childTag, substitutions);
            if (parent!=null && child!=null && c!=null) {
                LayoutManagerSupport layoutManagerSupport = LayoutManagerSupportFactory.createLayout(parent);
                JComponent cw = childTag.getComponentWrapper();
                layoutManagerSupport.addComponent(c, cw, childTag.getAlign());
//                layoutManagerSupport.addComponent(c, child, childTag.getAlign());
            }
        }
    }


    /**
     * Adds child tag. Parent tag and dom-model are assigned to child.
     * This does not affect on swing components, so this methos is useless after converting to swing.
     * @param tag child tag
     */
    public void addChild(Tag tag) {
        tag.setParent(this);
        tag.setModel(getModel());
        tag.createContextModel();
        children.add(tag);
    }

    /**
     * Adds child tag. Parent tag and dom-model are assigned to child.
     * This does not affect on swing components, so this methos is useless after converting to swing.
     * @param tag child tag
     * @param index index of the child elements array, into which the tag is inserted.
     */
    public void addChild(Tag tag, int index) {
        tag.setParent(this);
        tag.setModel(getModel());
        tag.createContextModel();
        children.add(index, tag);
    }

    /**
     * Removes child tag.
     * This does not affect on swing components, so this methos is useless after converting to swing.
     * @param tag child tag
     */
    public void removeChild(Tag tag) {
        tag.setParent(null);
        children.remove(tag);
    }

    public List<Tag> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tag getParent() {
        return parent;
    }

    public void setParent(Tag parent) {
        this.parent = parent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(String textAlign) {
        this.textAlign = textAlign;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVerticalAlign() {
        return verticalAlign;
    }

    public void setVerticalAlign(String verticalAlign) {
        this.verticalAlign = verticalAlign;
    }

    public JComponent getComponent() {
        return component;
    }

    public void setComponent(JComponent component) {
        this.component = component;
        this.setComponentWrapper(component);
    }

    public JComponent getComponentWrapper() {
        return componentWrapper;
    }

    public void setComponentWrapper(JComponent componentWrapper) {
        this.componentWrapper = componentWrapper;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontWeight() {
        return fontWeight;
    }

    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public JComponent createComponent() {
        logger.warn("Can't create component for tag "+getName());
        return null;
    }

    public void applyAttributes(JComponent component) {
        actualApplyAttributes(component, getAttributes());
    }

    /**
     * Применяет аттрибуты тэга к компоненту. Так как в атрибутах может храниться что угодно (например, стили),
     * данный метод должен обрабатывать каждый атрибут отдельно.
     * @param component компонент, для которого применяются атрибуты
     * @param attributes атрибуты
     */
    private void actualApplyAttributes(final JComponent component, Map<String, String> attributes) {
        if (component==null) {
            return;
        }
        for (String attrName : attributes.keySet()) {

            final String attrValue = attributes.get(attrName);

            if ("text".equals(attrName)) {
                try {
                    Method m = component.getClass().getMethod("setText", String.class);
                    m.invoke(component, attrValue);
                } catch (NoSuchMethodException e) {
                    logger.warn("Failed to set text property for component of class "+component.getClass());
                } catch (IllegalAccessException e) {
                    logger.warn("Failed to set text property for component of class "+component.getClass());
                } catch (InvocationTargetException e) {
                    logger.warn("Failed to set text property for component of class "+component.getClass());
                }
            }
            else if ("opaque".equals(attrName)) {
                component.setOpaque(Boolean.parseBoolean(attrValue));
            }
            else if ("icon".equals(attrName) && StringUtils.isNotBlank(attrValue)) {
                Icon icon = null;
                try {
                    icon = new ImageIcon(getClass().getResource(attrValue));
                } catch (Exception e) {
                    logger.warn("Can't load icon from resource '"+attrValue+"': "+e.getMessage());
                }

                if (icon!=null) {

                    try {
                        Method m = component.getClass().getMethod("setIcon", Icon.class);
                        m.invoke(component, icon);
                    } catch (NoSuchMethodException e) {
                        logger.warn("Failed to set icon property for component of class "+component.getClass());
                    } catch (IllegalAccessException e) {
                        logger.warn("Failed to set icon property for component of class "+component.getClass());
                    } catch (InvocationTargetException e) {
                        logger.warn("Failed to set icon property for component of class "+component.getClass());
                    }
                }

            }
            else if ("border".equals(attrName)) {
                component.setBorder(ru.swing.html.BorderFactory.createBorder(this));
            }
            else if ("enabled".equals(attrName)) {
                component.setEnabled((Boolean) Utils.convertStringToObject(attrValue, Boolean.class));
            }
            else if ("min-width".equals(attrName)) {
                Dimension d = component.getMinimumSize();
                d.setSize((Double) Utils.convertStringToObject(attrValue, Double.class), d.getHeight());
                component.setMinimumSize(d);
            }
            else if ("min-height".equals(attrName)) {
                Dimension d = component.getMinimumSize();
                d.setSize(d.getWidth(), (Double) Utils.convertStringToObject(attrValue, Double.class));
                component.setMinimumSize(d);
            }
            else if ("width".equals(attrName)) {
                Dimension d = component.getPreferredSize();
                d.setSize((Double) Utils.convertStringToObject(attrValue, Double.class), d.getHeight());
                component.setPreferredSize(d);
            }
            else if ("height".equals(attrName)) {
                Dimension d = component.getPreferredSize();
                d.setSize(d.getWidth(), (Double) Utils.convertStringToObject(attrValue, Double.class));
                component.setPreferredSize(d);
            }
            else if ("max-width".equals(attrName)) {
                Dimension d = component.getMaximumSize();
                d.setSize((Double) Utils.convertStringToObject(attrValue, Double.class), d.getHeight());
                component.setMaximumSize(d);
            }
            else if ("max-height".equals(attrName)) {
                Dimension d = component.getMaximumSize();
                d.setSize(d.getWidth(), (Double) Utils.convertStringToObject(attrValue, Double.class));
                component.setMaximumSize(d);
            }
            else if ("background-image".equals(attrName)) {
                try {
                    ImageIcon staticBkg = new ImageIcon(getClass().getResource(attrValue));
                    String hover = getAttribute("background-image-hover");
                    String clicked = getAttribute("background-image-clicked");
                    if (StringUtils.isEmpty(hover)) {
                        hover = attrValue;
                    }
                    if (StringUtils.isEmpty(clicked)) {
                        clicked = attrValue;
                    }
                    ImageIcon hoverBkg = new ImageIcon(getClass().getResource(hover));
                    ImageIcon clickedBkg = new ImageIcon(getClass().getResource(clicked));

                    BackgroundLayerUI backgroundLayerUI = new BackgroundLayerUI(staticBkg, hoverBkg, clickedBkg, staticBkg);
                    JXLayer layer = new JXLayer(component, backgroundLayerUI);
                    setComponentWrapper(layer);

                } catch (Exception e) {
                    logger.warn("Can't load icon from resource '"+attrValue+"': "+e.getMessage());
                }
            }


        }


        String resolvedId = ELUtils.parseStringValue(getId(), getModelElements());
        setId(resolvedId);

        if (StringUtils.isNotEmpty(getAttribute("readonly")) && component instanceof JTextComponent) {
            ((JTextComponent)component).setEditable(!Boolean.valueOf(getAttribute("readonly")));
        }


        if (component.getFont()!=null) {
            if (getFontSize()!=null) {
                component.setFont(component.getFont().deriveFont(new Float(getFontSize())));
            }

            Map<AttributedCharacterIterator.Attribute, Object> map = new HashMap<AttributedCharacterIterator.Attribute, Object>();
            if ("bold".equals(getFontWeight()) || "bolder".equals(getFontWeight())) {
                map.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
            }
            else if ("normal".equals(getFontWeight()) || "lighter".equals(getFontWeight())) {
                map.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
            }
            if (getFontFamily()!=null) {
                map.put(TextAttribute.FAMILY, getFontFamily());
            }

            Font font;
            if (map.size()>0) {
                font = component.getFont().deriveFont(map);
            }
            else {
                font = component.getFont();
            }
            if ("italic".equals(getFontStyle())) {
                font = font.deriveFont(font.getStyle()+Font.ITALIC);
            }
            component.setFont(font);
        }



        if (getColor()!=null) {
            component.setForeground(getColor());
        }

        if (getBackgroundColor()!=null) {
            component.setBackground(getBackgroundColor());
        }



        if (StringUtils.isNotEmpty(getTextAlign())) {
            float alignment = JComponent.CENTER_ALIGNMENT;
            if ("left".equals(getTextAlign())) {
                alignment = JComponent.LEFT_ALIGNMENT;
            }
            else if ("right".equals(getTextAlign())) {
                alignment = JComponent.RIGHT_ALIGNMENT;
            }
            component.setAlignmentX(alignment);
        }

        if (StringUtils.isNotEmpty(getVerticalAlign())) {
            float alignment = JComponent.CENTER_ALIGNMENT;
            if ("top".equals(getVerticalAlign())) {
                alignment = JComponent.TOP_ALIGNMENT;
            }
            else if ("bottom".equals(getVerticalAlign())) {
                alignment = JComponent.BOTTOM_ALIGNMENT;
            }
            component.setAlignmentY(alignment);
        }


        //если задан атрибут onclick и компонент - это кнопка (то есть ее можно нажать),
        //то значение атрибута - название метода в контроллере, который необходимо
        //вызвать при нажатии
        final String onclickMethod = getAttribute("onclick");
        if (StringUtils.isNotEmpty(onclickMethod) && (component instanceof AbstractButton)) {

            final Object controller = model.getController();
            if (controller!=null) {

                //находим требуемый метод
                Method method = Utils.findActionMethod(controller.getClass(), onclickMethod, ActionEvent.class);

                //если метод нашелся, то добавляем к компоненту слушатель, который вызывает метод.
                if (method!=null) {
                    //добавляем слушатель, который вызывает метод
                    AbstractButton b = (AbstractButton) component;
                    if (clickDelegator !=null) {
                        b.removeActionListener(clickDelegator);
                    }
                    clickDelegator = new ClickDelegator(controller, method);
                    b.addActionListener(clickDelegator);
                }
                else {
                    logger.warn("Can't find method " +onclickMethod+" in class "+controller.getClass().getName());
                }
            }
        }


        //если задан атрибут onchange и компонент - это текстовое поле (то есть у нее есть документ),
        //то значение атрибута - название метода в контроллере, который необходимо
        //вызвать при изменении документа
        final String onchangeMethod = getAttribute("onchange");
        if (StringUtils.isNotEmpty(onchangeMethod) && (component instanceof JTextComponent)) {

            final Object controller = model.getController();
            if (controller!=null) {

                //находим требуемый метод
                Method method = Utils.findActionMethod(controller.getClass(), onchangeMethod, DocumentEvent.class);

                //если метод нашелся, то добавляем к компоненту слушатель, который вызывает метод.
                if (method!=null) {
                    //добавляем слушатель, который вызывает метод
                    JTextComponent b = (JTextComponent) component;
                    if (documentDelegator!=null) {
                        b.getDocument().removeDocumentListener(documentDelegator);
                    }
                    documentDelegator = new DocumentDelegator(controller, method);
                    b.getDocument().addDocumentListener(documentDelegator);
                }
                else {
                    logger.warn("Can't find method " +onchangeMethod+" in class "+controller.getClass().getName());
                }
            }
        }



    }

    public DomModel getModel() {
        return model;
    }

    public void setModel(DomModel model) {
        this.model = model;
    }

    /**
     * This call-back method is called in DomLoader after child elements are loaded and appended to this tag.
     */
    public void afterChildElementsConverted() {
    }

    /**
     * This call-back method is called in DomLoader before convertion to swing is started.
     */
    public void beforeComponentsConvertion() {
    }

    /**
     * This method is called in DomConverter after all tags are parsed and converted to components.
     */
    public void afterComponentsConverted() {
    }


    public String toString() {
        List<String> pathToRoot = new ArrayList<String>();
        Tag pos = this;
        while (pos!=null) {
            StringBuilder sb = new StringBuilder(pos.getName());
            if (StringUtils.isNotEmpty(pos.getId())) {
                sb.append("[#").append(pos.getId()).append("]");
            }
            pathToRoot.add(sb.toString());
            pos = pos.getParent();
        }

        Collections.reverse(pathToRoot);
        return StringUtils.join(pathToRoot.iterator(), " > ");

    }



    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public Tag clone()  {
        Tag clone = DomLoader.createTag(getNamespace(), getName());
        clone.setModel(model);
        for (String attrName : attributes.keySet()) {
            clone.setAttribute(attrName, attributes.get(attrName));
        }
        clone.setContent(content);

        for (Tag child : getChildren()) {
            Tag childClone = child.clone();
            clone.addChild(childClone);
        }

        return clone;
    }

    public void addModelElement(String name, Object value) {
        modelElements.put(name, value);
    }

    public Map<String, Object> getModelElements() {
        //if this is root tag, when noone will call "addChild", so local model is not created from dom model
        if (getParent()==null && modelElements==null) {
            createContextModel();
        }
        return modelElements;
    }

    /**
     * Creates map with model elements, where local elements take preference over global.
     * @return
     */
    public Map<String, Object> createContextModel() {


        //first remove listener from old parent map
        if (parentMap!=null && parentMapListener!=null) {
            parentMap.removeObservableMapListener(parentMapListener);
        }

        if (getParent()==null && getModel()!=null) {
            parentMap = (ObservableMap) getModel().getModelElements();
        }
        else if (getParent()!=null) {
            parentMap = (ObservableMap) getParent().getModelElements();
        }
        else {
            parentMap = null;
        }

        
        modelElements = parentMap !=null ? ObservableCollections.observableMap(new HashMap(parentMap)) :
                ObservableCollections.observableMap(new HashMap());

        if (parentMap !=null) {
            //sync with parent map
            parentMapListener = new ObservableMapListener() {
                public void mapKeyValueChanged(ObservableMap map, Object key, Object lastValue) {
                    Object newValue = map.get(key);
                    modelElements.put((String) key, newValue);
                    logger.trace(Tag.this.toString()+": updated model element '"+key+"' to value: "+newValue);
                }

                public void mapKeyAdded(ObservableMap map, Object key) {
                    modelElements.put((String) key, map.get(key));
                }

                public void mapKeyRemoved(ObservableMap map, Object key, Object value) {
                    modelElements.remove(key);
                }
            };
            parentMap.addObservableMapListener(parentMapListener);
        }

        return modelElements;

    }

    public void bind(String elPath, JComponent component, Property componentProperty, AutoBinding.UpdateStrategy type) {

        ELProperty<Map<String, Object>, String> beanProperty = ELProperty.create(elPath);
        Map<String, Object> modelElements1 = getModelElements();
        Binding binding = Bindings.createAutoBinding(type, modelElements1, beanProperty, component, componentProperty);
        binding.bind();
        logger.debug(toString()+ ": binded '"+elPath+"'");

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (attributes != null ? !attributes.equals(tag.attributes) : tag.attributes != null) return false;
        if (id != null ? !id.equals(tag.id) : tag.id != null) return false;
        if (name != null ? !name.equals(tag.name) : tag.name != null) return false;
        if (namespace != null ? !namespace.equals(tag.namespace) : tag.namespace != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (namespace != null ? namespace.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        return result;
    }

    private static class BackgroundLayerUI extends AbstractLayerUI {
        private ImageIcon staticBkg;
        private ImageIcon hoverBkg;
        private ImageIcon clickedBkg;
        private ImageIcon disabledBkg;
        private int lastMouseState = MouseEvent.MOUSE_FIRST;


        public BackgroundLayerUI(ImageIcon staticBkg, ImageIcon hoverBkg, ImageIcon clickedBkg, ImageIcon disabledBkg) {
            this.staticBkg = staticBkg;
            this.hoverBkg = hoverBkg;
            this.clickedBkg = clickedBkg;
            this.disabledBkg = disabledBkg;
        }

        @Override
        protected void paintLayer(Graphics2D graphics2D, JXLayer jxLayer) {
            int state = 0;
            //mouse over
            if (jxLayer.getMousePosition()!=null) {
                state = lastMouseState;
            }
            else {
                state = 0;
            }

            if (!jxLayer.getView().isEnabled()) {
                state = -1;
            }


            ImageIcon img;
            switch (state) {
                case -1 : img = disabledBkg; break;
                case 0 : img = staticBkg; break;
                case MouseEvent.MOUSE_PRESSED : img = clickedBkg; break;
                case MouseEvent.MOUSE_RELEASED : img = hoverBkg; break;
                default: img = jxLayer.getMousePosition() != null ? hoverBkg : staticBkg; break;
            }
            graphics2D.drawImage(img.getImage(), 0, 0, null);


            super.paintLayer(graphics2D, jxLayer);
        }


        @Override
        protected void processMouseEvent(MouseEvent mouseEvent, JXLayer jxLayer) {
            super.processMouseEvent(mouseEvent, jxLayer);
            lastMouseState = mouseEvent.getID();
        }
    }
}
