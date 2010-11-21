package ru.swing.html.tags;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.*;
import ru.swing.html.layout.LayoutManagerSupport;
import ru.swing.html.layout.LayoutManagerSupportFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 10:47:28
 * </pre>
 */
public class Tag {

    private JComponent component;
    private Log logger = LogFactory.getLog(Tag.class);
    private String id;
    private String name;
    private Tag parent;
    private List<Tag> children = new ArrayList<Tag>();
    private String content;
    private String layout;
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
    private int width;
    private int height;
    private Map<String, String> attributes = new HashMap<String, String>();

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
        if ("layout".equals(name)) {
            setLayout(value);
        }
        else if ("id".equals(name)) {
            setId(value);
        }
        else if ("align".equals(name)) {
            setAlign(value);
        }
        else if ("text-align".equals(name)) {
            setTextAlign(value);
        }
        else if ("type".equals(name)) {
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
            setWidth(new Integer(value));
        }
        else if ("height".equals(name)) {
            setHeight(new Integer(value));
        }
        else if ("style".equals(name)) {
            Map<String, String> styles = StyleParser.extractStyles(value);
            for (String styleName : styles.keySet()) {
                setAttribute(styleName, styles.get(styleName));
            }
        }
        else {
            attributes.put(name, value);
        }
    }

    public void handleLayout() {
        final String layoutName = getLayout();
        LayoutManagerSupport layoutManagerSupport = null;
        if (layoutName != null) {
            layoutManagerSupport = LayoutManagerSupportFactory.createLayout(this);
            component.setLayout(layoutManagerSupport.createLayout(this));
        }

    }

    public void handleChildren() {
        for (Tag childTag : getChildren()) {
            JComponent child = DomConverter.convertComponent(childTag);
            if (getLayout() != null) {
                LayoutManagerSupport layoutManagerSupport = LayoutManagerSupportFactory.createLayout(this);
                layoutManagerSupport.addComponent(component, child, childTag.getAlign());
            }
        }

    }


    public List<Tag> getChildren() {
        return children;
    }

    public void setChildren(List<Tag> children) {
        this.children = children;
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

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
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
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
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

    public JComponent createComponent() {
        logger.error("Can't create component for tag "+getName());
        return null;
    }

    /**
     * Применяет аттрибуты тэга к компоненту. Так как в атрибутах может храниться что угодно (например, стили),
     * данный метод должен обрабатывать каждый атрибут отдельно.
     * @param component компонент, для которого применяются атрибуты
     */
    public void applyAttributes(JComponent component) {
        final Map<String,String> attributes = getAttributes();
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
            else if ("icon".equals(attrName)) {
                Icon icon = new ImageIcon(getClass().getResource(attrValue));
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
            else if ("border".equals(attrName)) {
                component.setBorder(ru.swing.html.BorderFactory.createBorder(this));
            }


        }

        if (getColor()!=null) {
            component.setForeground(getColor());
        }

        if (getBackgroundColor()!=null) {
            component.setBackground(getBackgroundColor());
        }
    }
}
