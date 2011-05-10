package ru.swing.html.configuration;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.jxlayer.JXLayer;
import ru.swing.html.ColorFactory;
import ru.swing.html.ELUtils;
import ru.swing.html.Utils;
import ru.swing.html.components.BackgroundImageLayerUI;
import ru.swing.html.tags.Tag;
import ru.swing.html.tags.event.ClickDelegator;
import ru.swing.html.tags.event.DocumentDelegator;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.font.TextAttribute;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the default attribute parser for tag. It parses common attributes. It also allows
 * attaching custom parsers via "setParserForAttribute" method. You can add new parsers for unsupported
 * attribues or override existing.
 */
public class DefaultAttributeParser implements AttributeParser {

    private Log logger = LogFactory.getLog(getClass());
    private ClickDelegator clickDelegator;
    private DocumentDelegator documentDelegator;
    private Map<String, AttributeParser> parsers = new HashMap<String, AttributeParser>();


    public DefaultAttributeParser() {
        setParserForAttribute("background-color", new BackgroundColorAttributeParser());
        setParserForAttribute("background-image", new BackgroundImageAttributeParser());
        setParserForAttribute("border", new BorderAttributeParser());
        setParserForAttribute("color", new ColorAttributeParser());
        setParserForAttribute("enabled", new EnabledAttributeParser());
        setParserForAttribute("font-family", new FontAttributeParser());
        setParserForAttribute("font-size", new FontSizeAttributeParser());
        setParserForAttribute("font-style", new FontAttributeParser());
        setParserForAttribute("font-weight", new FontAttributeParser());
        setParserForAttribute("height", new HeightAttributeParser());
        setParserForAttribute("id", new IdAttributeParser());
        setParserForAttribute("onclick", new OnclickAttributeParser());
        setParserForAttribute("onchange", new OnchangeAttributeParser());
        setParserForAttribute("opaque", new OpaqueAttributeParser());
        setParserForAttribute("icon", new IconAttributeParser());
        setParserForAttribute("max-height", new MaxHeightAttributeParser());
        setParserForAttribute("max-width", new MaxWidthAttributeParser());
        setParserForAttribute("min-height", new MinHeightAttributeParser());
        setParserForAttribute("min-width", new MinWidthAttributeParser());
        setParserForAttribute("readonly", new ReadOnlyAttributeParser());
        setParserForAttribute("text", new TextAttributeParser());
        setParserForAttribute("text-align", new TextAlignAttributeParser());
        setParserForAttribute("vertical-align", new VerticalAlignAttributeParser());
        setParserForAttribute("width", new WidthAttributeParser());
    }


    /**
     * Sets parser for supplied attribute. If already contains parser for this attribute, existing will
     * be overriden.
     * @param attributeName the name og the attribute, e.g. "border"
     * @param parser parser for the attribute
     */
    public void setParserForAttribute(String attributeName, AttributeParser parser) {
        parsers.put(attributeName, parser);
    }


    public void applyAttribute(Tag tag, JComponent component, String attrName) {
        if (component==null) {
            return;
        }

        if (parsers.containsKey(attrName)) {
            parsers.get(attrName).applyAttribute(tag, component, attrName);
        }

    }

    class TextAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            try {
                Method m = component.getClass().getMethod("setText", String.class);
                String text = ELUtils.parseStringValue(tag.getText(), tag.getModelElements());
                m.invoke(component, text);
            } catch (NoSuchMethodException e) {
                logger.warn("Failed to set text property for component of class "+component.getClass());
            } catch (IllegalAccessException e) {
                logger.warn("Failed to set text property for component of class "+component.getClass());
            } catch (InvocationTargetException e) {
                logger.warn("Failed to set text property for component of class "+component.getClass());
            }
        }
    }

    class OpaqueAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            String opaque = ELUtils.parseStringValue(tag.getAttribute("opaque"), tag.getModelElements());
            component.setOpaque(Boolean.parseBoolean(opaque));
        }
    }

    class IconAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            Icon icon = null;
            try {
                String val = ELUtils.parseStringValue(tag.getAttribute("icon"), tag.getModelElements());
                Image image = ImageIO.read(tag.getModel().getConfiguration().getResourceLoader().loadResource(tag.getModel(), val));
                icon = new ImageIcon(image);
            } catch (Exception e) {
                logger.warn("Can't load icon from resource '"+tag.getAttribute("icon")+"': "+e.getMessage());
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
    }

    class BorderAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            component.setBorder(ru.swing.html.BorderFactory.createBorder(tag));
        }
    }

    class EnabledAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            String enabled = ELUtils.parseStringValue(tag.getAttribute("enabled"), tag.getModelElements());
            component.setEnabled(Utils.convertStringToObject(enabled, Boolean.class));
        }
    }

    class MinWidthAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            String minWidth = ELUtils.parseStringValue(tag.getAttribute("min-width"), tag.getModelElements());
            Dimension d = component.getMinimumSize();
            d.setSize(Utils.convertStringToObject(minWidth, Double.class), d.getHeight());
            component.setMinimumSize(d);
        }
    }

    class MinHeightAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            String minHeight = ELUtils.parseStringValue(tag.getAttribute("min-height"), tag.getModelElements());
            Dimension d = component.getMinimumSize();
            d.setSize(d.getWidth(), Utils.convertStringToObject(minHeight, Double.class));
            component.setMinimumSize(d);
        }
    }

    class WidthAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            String width = ELUtils.parseStringValue(tag.getAttribute("width"), tag.getModelElements());
            Dimension d = component.getPreferredSize();
            d.setSize(Utils.convertStringToObject(width, Double.class), d.getHeight());
            component.setPreferredSize(d);
        }
    }

    class HeightAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            String height = ELUtils.parseStringValue(tag.getAttribute("height"), tag.getModelElements());
            Dimension d = component.getPreferredSize();
            d.setSize(d.getWidth(), Utils.convertStringToObject(height, Double.class));
            component.setPreferredSize(d);
        }
    }

    class MaxWidthAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            String maxWidth = ELUtils.parseStringValue(tag.getAttribute("max-width"), tag.getModelElements());
            Dimension d = component.getMaximumSize();
            d.setSize(Utils.convertStringToObject(maxWidth, Double.class), d.getHeight());
            component.setMaximumSize(d);
        }
    }

    class MaxHeightAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            String maxHeight = ELUtils.parseStringValue(tag.getAttribute("max-height"), tag.getModelElements());
            Dimension d = component.getMaximumSize();
            d.setSize(d.getWidth(), Utils.convertStringToObject(maxHeight, Double.class));
            component.setMaximumSize(d);
        }
    }

    class BackgroundImageAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            String staticB = ELUtils.parseStringValue(tag.getAttribute("background-image"), tag.getModelElements());
            String hover = ELUtils.parseStringValue(tag.getAttribute("background-image-hover"), tag.getModelElements());
            String clicked = ELUtils.parseStringValue(tag.getAttribute("background-image-clicked"), tag.getModelElements());

            try {
                ImageIcon staticBkg = new ImageIcon(ImageIO.read(tag.getModel().getConfiguration().getResourceLoader().loadResource(tag.getModel(), staticB)));
                if (StringUtils.isEmpty(hover)) {
                    hover = staticB;
                }
                if (StringUtils.isEmpty(clicked)) {
                    clicked = staticB;
                }
                ImageIcon hoverBkg = new ImageIcon(ImageIO.read(tag.getModel().getConfiguration().getResourceLoader().loadResource(tag.getModel(), hover)));
                ImageIcon clickedBkg = new ImageIcon(ImageIO.read(tag.getModel().getConfiguration().getResourceLoader().loadResource(tag.getModel(), clicked)));

                BackgroundImageLayerUI backgroundLayerUI = new BackgroundImageLayerUI(staticBkg, hoverBkg, clickedBkg, staticBkg);
                JXLayer layer = new JXLayer<JComponent>(component, backgroundLayerUI);
                tag.setComponentWrapper(layer);

            } catch (Exception e) {
                logger.warn("Can't load icon from resource '"+staticB+"': "+e.getMessage());
            }
        }
    }

    class IdAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            String resolvedId = ELUtils.parseStringValue(tag.getId(), tag.getModelElements());
            tag.setId(resolvedId);
        }
    }

    class ReadOnlyAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            if (StringUtils.isNotEmpty(tag.getAttribute("readonly")) && component instanceof JTextComponent) {
                String readonly = ELUtils.parseStringValue(tag.getAttribute("readonly"), tag.getModelElements());
                ((JTextComponent)component).setEditable(!Boolean.valueOf(readonly));
            }
        }
    }

    class FontSizeAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            if (component.getFont()!=null) {
                if (StringUtils.isNotEmpty(tag.getFontSize())) {
                    String fontSize = ELUtils.parseStringValue(tag.getFontSize(), tag.getModelElements());
                    component.setFont(component.getFont().deriveFont(new Float(fontSize)));
                }
            }
        }
    }

    class FontAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            if (component.getFont()!=null) {
                Map<AttributedCharacterIterator.Attribute, Object> map = new HashMap<AttributedCharacterIterator.Attribute, Object>();
                String fontWeight = ELUtils.parseStringValue(tag.getFontWeight(), tag.getModelElements());
                if ("bold".equals(fontWeight) || "bolder".equals(fontWeight)) {
                    map.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
                }
                else if ("normal".equals(fontWeight) || "lighter".equals(fontWeight)) {
                    map.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
                }
                if (tag.getFontFamily()!=null) {
                    map.put(TextAttribute.FAMILY, tag.getFontFamily());
                }

                Font font;
                if (map.size()>0) {
                    font = component.getFont().deriveFont(map);
                }
                else {
                    font = component.getFont();
                }
                String fontStyle = ELUtils.parseStringValue(tag.getFontStyle(), tag.getModelElements());
                if ("italic".equals(fontStyle)) {
                    font = font.deriveFont(font.getStyle()+Font.ITALIC);
                }
                component.setFont(font);
            }
        }
    }

    class ColorAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            String colorValue = ELUtils.parseStringValue(tag.getColor(), tag.getModelElements());
            Color color = ColorFactory.getColor(colorValue);
            component.setForeground(color);
        }
    }

    class BackgroundColorAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            String value = ELUtils.parseStringValue(tag.getBackgroundColor(), tag.getModelElements());
            Color color = ColorFactory.getColor(value);
            component.setBackground(color);
        }
    }

    class TextAlignAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            String textAlign = ELUtils.parseStringValue(tag.getTextAlign(), tag.getModelElements());
            float alignment = JComponent.CENTER_ALIGNMENT;
            if ("left".equals(textAlign)) {
                alignment = JComponent.LEFT_ALIGNMENT;
            }
            else if ("right".equals(textAlign)) {
                alignment = JComponent.RIGHT_ALIGNMENT;
            }
            component.setAlignmentX(alignment);
        }
    }

    class VerticalAlignAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            String verticalAlign = ELUtils.parseStringValue(tag.getVerticalAlign(), tag.getModelElements());
            float alignment = JComponent.CENTER_ALIGNMENT;
            if ("top".equals(verticalAlign)) {
                alignment = JComponent.TOP_ALIGNMENT;
            }
            else if ("bottom".equals(verticalAlign)) {
                alignment = JComponent.BOTTOM_ALIGNMENT;
            }
            component.setAlignmentY(alignment);
        }
    }

    class OnclickAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            //если задан атрибут onclick и компонент - это кнопка (то есть ее можно нажать),
            //то значение атрибута - название метода в контроллере, который необходимо
            //вызвать при нажатии
            final String onclickMethod = tag.getAttribute("onclick");
            if (StringUtils.isNotEmpty(onclickMethod) && (component instanceof AbstractButton)) {

                final Object controller = tag.getModel().getController();
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
    }

    class OnchangeAttributeParser implements AttributeParser {

        public void applyAttribute(Tag tag, JComponent component, String attrName) {
            //если задан атрибут onchange и компонент - это текстовое поле (то есть у нее есть документ),
            //то значение атрибута - название метода в контроллере, который необходимо
            //вызвать при изменении документа
            final String onchangeMethod = tag.getAttribute("onchange");
            if (StringUtils.isNotEmpty(onchangeMethod) && (component instanceof JTextComponent)) {

                final Object controller = tag.getModel().getController();
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


}
