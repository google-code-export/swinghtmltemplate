package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.*;
import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableMap;
import org.jdesktop.observablecollections.ObservableMapListener;
import ru.swing.html.*;
import ru.swing.html.components.BackgroundImageLayerUI;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.css.StyleParser;
import ru.swing.html.layout.LayoutManagerSupport;
import ru.swing.html.layout.LayoutManagerSupportFactory;
import ru.swing.html.tags.event.ClickDelegator;
import ru.swing.html.tags.event.DocumentDelegator;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.font.TextAttribute;
import java.lang.Boolean;
import java.lang.Cloneable;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Float;
import java.lang.IllegalAccessException;
import java.lang.NoSuchMethodException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
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

    public static final String TAG_CONTENT = "tag-content";
    private DomModel model;
    private JComponent component;
    /**
     * This is an component, which will be actually added to the parent's component.
     * Most of the time is is the same as 'component', but you can wrap original component
     * with this wrapper for some needs (e.g. use JXLayer).
     */
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
    private String text;
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
        return getAttributes().get(name);
    }


    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * <p>
     * Sets attribute to the tag. Successors must call super method, when override.
     * Default implementation sets common propeties ("display", "id" etc), parses
     * "style" attribute (splits it into token and invokes setAttribute for each token)
     * and stores all atrinutes in local map (that is why invoking super is needed).
     * </p>
     * <p>
     *     This method is invoked in DomLoader when loading document and converting it to the dom model.
     * </p>

     * @param name the name of the attribute
     * @param value the string representation if the value for attribute
     */
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
        else if (TAG_CONTENT.equals(name)) {
            setContent(value);
        }
        else if ("text".equals(name)) {
            setText(value);
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

    /**
     * In this method tag must set layout for the component. Default implementation
     * creates LayoutManagerSupport with the help of LayoutManagerSupportFactory,
     * then it creates LayoutManager from LayoutManagerSupport and assigns it to the component.
     * <p>
     * This method is called by the DomConverter after "applyAttributes"
     * method in "component-conversion" phase.
     * </p>
     * @see LayoutManagerSupport
     * @see LayoutManagerSupportFactory
     */
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
     * <p>
     * This method is called by the DomConverter after "handleLayout"
     * method in "component-conversion" phase.
     * </p>
     * <p>
     *     The default implementation do the following:
     *     <ul>
     *         <li>if tag's component is null (this tag do not produce any component), when find first
     *         parent tag with non-null component, if noone is found, exit</li>
     *         <li>Create LayoutManagerSupport for founded tag using LayoutManagerSupportFactory</li>
     *         TODO creating LayoutManagerSupport is done twice, first in handleLayout, 2nd - here
     *         <li>for every child: converts tag using DomConverter.convertComponent and add child's componentWrapper
     *         (if it is not null) to founded parent using LayoutManagerSupport</li>
     *      </ul>
     * </p>
     * @param substitutions substitutions map for domModel. The only need of this parameter is to pass it to DomConverter
     * in default implementation for converting child tag.
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

        if (parent!=null) {
            LayoutManagerSupport layoutManagerSupport = LayoutManagerSupportFactory.createLayout(parent);
            for (Tag childTag : getChildren()) {
                JComponent child = DomConverter.convertComponent(childTag, substitutions);
                if (child!=null) {
                    JComponent cw = childTag.getComponentWrapper();
                    layoutManagerSupport.addComponent(c, cw, childTag.getAlign());
    //                layoutManagerSupport.addComponent(c, child, childTag.getAlign());
                }
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

    protected void setContent(String content) {
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

    /**
     * Sets the component for this tag. This will also set componentWrapper with the same argument.
     * @param component component for this tag
     */
    public void setComponent(JComponent component) {
        this.component = component;
        this.setComponentWrapper(component);
    }

    /**
     * returns the component wrapper.
     * @return component wrapper
     * @see #componentWrapper
     */
    public JComponent getComponentWrapper() {
        return componentWrapper;
    }

    /**
     * Sets the component wrapper. It will be actually added to the parent tag's component.
     * @param componentWrapper component wrapper.
     * @see #componentWrapper
     */
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

    /**
     * <p>
     * Implementations must create tag's component in this method. Some initial properties
     * can be set for the component here too.
     * </p>
     * <p>
     *     This method is invoked in DomConverter during 'component-conversion' phase after invoking 'setAttribute()'.
     *     After invoking this method DomConverter invokes "setComponent" with the value, returned from this methos, as argument.
     * </p>
     * @return created component for the tag.
     * @see DomConverter#convertComponent(Tag)
     * @see #setComponent(javax.swing.JComponent)
     */
    public JComponent createComponent() {
        logger.warn("Can't create component for tag "+getName());
        return null;
    }

    /**
     * <p>
     * Implementations must apply tag's attribute to the component, individual to that tag.
     * Default implementation applies common
     * attributes, such as 'text', 'font', 'opaque' etc.
     * </p>
     * <p>
     *     This method is called by the DomConverter after "createComponent" and "setComponent" method
     *     in "component-conversion" phase
     * </p>    
     * @param component the component, to which attributes must be applied to
     */
    public final void applyAttributes(JComponent component) {
        for (String attrName : getAttributes().keySet()) {
            applyAttribute(component, attrName);
        }
        applyAttribute(component, TAG_CONTENT);

    }


    /**
     * <p>
     *     Apply attribute with specified name to the component. The value for the attribute must be
     * got by accessor using setter for the corresponding property, if it exists, or using getAttribute(attrName) if not.
     * </p>
     * <p>
     *     E.g. for the attribute "color" there exists property "color" of type java.awt.Color. So to get
     *     property value getColor() is used, rather then "getAttribute("color");". This way we
     *     assure that all convertion (if it is needed) is done in "setAttribute()" method.
     * <p>
     * Thus, to change existing attribute you must first call "setAttribute(name, value)"
     * and then "applyAttribute(component, name)".
     * </p>
     * <p>
     *     Special attribute named "tag-content" must be treated as tag's content. It's value can be accessed via
     *     "getContent();"
     * </p>
     * @param component component, attribute value will be applied to it
     * @param attrName the name of the attribute to apply.
     */
    public void applyAttribute(JComponent component, String attrName) {
        if (component==null) {
            return;
        }
        if ("text".equals(attrName)) {
            try {
                Method m = component.getClass().getMethod("setText", String.class);
                m.invoke(component, getText());
            } catch (NoSuchMethodException e) {
                logger.warn("Failed to set text property for component of class "+component.getClass());
            } catch (IllegalAccessException e) {
                logger.warn("Failed to set text property for component of class "+component.getClass());
            } catch (InvocationTargetException e) {
                logger.warn("Failed to set text property for component of class "+component.getClass());
            }
        }
        else if ("opaque".equals(attrName)) {
            component.setOpaque(Boolean.parseBoolean(getAttribute("opaque")));
        }
        else if ("icon".equals(attrName) && StringUtils.isNotBlank(getAttribute("icon"))) {
            Icon icon = null;
            try {
                Image image = ImageIO.read(getModel().getConfiguration().getResourceLoader().loadResource(getModel(), getAttribute("icon")));
                icon = new ImageIcon(image);
            } catch (Exception e) {
                logger.warn("Can't load icon from resource '"+getAttribute("icon")+"': "+e.getMessage());
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
            component.setEnabled(Utils.convertStringToObject(getAttribute("enabled"), Boolean.class));
        }
        else if ("min-width".equals(attrName)) {
            Dimension d = component.getMinimumSize();
            d.setSize(Utils.convertStringToObject(getAttribute("min-width"), Double.class), d.getHeight());
            component.setMinimumSize(d);
        }
        else if ("min-height".equals(attrName)) {
            Dimension d = component.getMinimumSize();
            d.setSize(d.getWidth(), Utils.convertStringToObject(getAttribute("min-height"), Double.class));
            component.setMinimumSize(d);
        }
        else if ("width".equals(attrName)) {
            Dimension d = component.getPreferredSize();
            d.setSize(Utils.convertStringToObject(getAttribute("width"), Double.class), d.getHeight());
            component.setPreferredSize(d);
        }
        else if ("height".equals(attrName)) {
            Dimension d = component.getPreferredSize();
            d.setSize(d.getWidth(), Utils.convertStringToObject(getAttribute("height"), Double.class));
            component.setPreferredSize(d);
        }
        else if ("max-width".equals(attrName)) {
            Dimension d = component.getMaximumSize();
            d.setSize(Utils.convertStringToObject(getAttribute("max-width"), Double.class), d.getHeight());
            component.setMaximumSize(d);
        }
        else if ("max-height".equals(attrName)) {
            Dimension d = component.getMaximumSize();
            d.setSize(d.getWidth(), Utils.convertStringToObject(getAttribute("max-height"), Double.class));
            component.setMaximumSize(d);
        }
        else if ("background-image".equals(attrName)) {

            String staticB = getAttribute("background-image");
            String hover = getAttribute("background-image-hover");
            String clicked = getAttribute("background-image-clicked");

            try {
                ImageIcon staticBkg = new ImageIcon(ImageIO.read(getModel().getConfiguration().getResourceLoader().loadResource(getModel(), staticB)));
                if (StringUtils.isEmpty(hover)) {
                    hover = staticB;
                }
                if (StringUtils.isEmpty(clicked)) {
                    clicked = staticB;
                }
                ImageIcon hoverBkg = new ImageIcon(ImageIO.read(getModel().getConfiguration().getResourceLoader().loadResource(getModel(), hover)));
                ImageIcon clickedBkg = new ImageIcon(ImageIO.read(getModel().getConfiguration().getResourceLoader().loadResource(getModel(), clicked)));

                BackgroundImageLayerUI backgroundLayerUI = new BackgroundImageLayerUI(staticBkg, hoverBkg, clickedBkg, staticBkg);
                JXLayer layer = new JXLayer(component, backgroundLayerUI);
                setComponentWrapper(layer);

            } catch (Exception e) {
                logger.warn("Can't load icon from resource '"+staticB+"': "+e.getMessage());
            }
        }
        else if ("id".equals(attrName)) {
            String resolvedId = ELUtils.parseStringValue(getId(), getModelElements());
            setId(resolvedId);
        }
        else if ("readonly".equals(attrName)) {
            if (StringUtils.isNotEmpty(getAttribute("readonly")) && component instanceof JTextComponent) {
                ((JTextComponent)component).setEditable(!Boolean.valueOf(getAttribute("readonly")));
            }
        }
        else if ("font-size".equals(attrName)) {
            if (component.getFont()!=null) {
                if (getFontSize()!=null) {
                    component.setFont(component.getFont().deriveFont(new Float(getFontSize())));
                }
            }
        }
        else if ("font-weight".equals(attrName) || "font-family".equals(attrName) || "font-style".equals(attrName)) {
            if (component.getFont()!=null) {
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
        }
        else if ("color".equals(attrName)) {
            component.setForeground(getColor());
        }
        else if ("background-color".equals(attrName)) {
            component.setBackground(getBackgroundColor());
        }
        else if ("text-align".equals(attrName)) {
            float alignment = JComponent.CENTER_ALIGNMENT;
            if ("left".equals(getTextAlign())) {
                alignment = JComponent.LEFT_ALIGNMENT;
            }
            else if ("right".equals(getTextAlign())) {
                alignment = JComponent.RIGHT_ALIGNMENT;
            }
            component.setAlignmentX(alignment);

        }
        else if ("vertical-align".equals(attrName)) {
            float alignment = JComponent.CENTER_ALIGNMENT;
            if ("top".equals(getVerticalAlign())) {
                alignment = JComponent.TOP_ALIGNMENT;
            }
            else if ("bottom".equals(getVerticalAlign())) {
                alignment = JComponent.BOTTOM_ALIGNMENT;
            }
            component.setAlignmentY(alignment);
        }
        else if ("onclick".equals(attrName)) {
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
        }
        else if ("onchange".equals(attrName)) {
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
    }



    public DomModel getModel() {
        return model;
    }

    public void setModel(DomModel model) {
        this.model = model;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        Tag clone = DomLoader.createTag(getModel().getConfiguration().getLibraryLoader().getLibraryRegistry(),
                getNamespace(), getName());
        clone.setModel(model);
        for (String attrName : attributes.keySet()) {
            clone.setAttribute(attrName, attributes.get(attrName));
        }
        clone.setAttribute(TAG_CONTENT, content);

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

}
