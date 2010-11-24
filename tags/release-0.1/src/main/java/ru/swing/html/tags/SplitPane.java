package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.DomConverter;
import ru.swing.html.DomModel;

import javax.swing.*;

/**
 * <p>
 * Обработчик тега &lt;split>.
 * </p>
 * <p>
 * Допустимые параметры тега:
 * </p>
 * <ul>
 * <li>orientation - ориентация, допустимые значения: horizontal, vertical</li>
 * <li>divider-size - ширина разделительной линии</li>
 * <li>divider-position - положение разделительной линии. в пикселях (100) или процентах (50%)</li>
 * </ul>
 *
 * <p>
 * У тега может быть ровно 2 дочерних элемента. Расположение элементов зависит от значения
 * атрибута align дочернего тега:
 * </p>
 * <ul>
 * <li>left - компонент пойдет в левую панель</li>
 * <li>top - компонент пойдет в верхнюю панель</li>
 * <li>right - компонент пойдет в правую панель</li>
 * <li>bottom - компонент пойдет в нижнюю панель</li>
 * </ul>
 * <p>
 * Если align не указан у первого тега, то первый компонент пойдет в левую панель, 2ой - в правую.
 * </p>
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 16:55:57
 * </pre>
 */
public class SplitPane extends Tag {
    private Log logger = LogFactory.getLog(getClass());
    private int orientation;
    private int dividerWidth;
    private Double dividerPercentLocation = null;
    private int dividerLocation = -1;
    public static final String ORIENTATION_ATTRIBUTE = "orientation";
    public static final String DIVIDER_SIZE_ATTRIBUTE = "divider-size";
    public static final String DIVIDER_POSITION_ATTRIBUTE = "divider-position";


    @Override
    public JComponent createComponent() {
        JSplitPane c = new JSplitPane();
        setComponent(c);
        return c;
    }
    
    @Override
    public void handleChildren() {
        if (getComponent() instanceof JSplitPane) {

            JSplitPane pane = (JSplitPane) getComponent();

            if (getChildren().size()==2) {
                JComponent childComponent1 = DomConverter.convertComponent(getChildren().get(0));
                JComponent childComponent2 = DomConverter.convertComponent(getChildren().get(1));

                placeChild(childComponent1, getChildren().get(0).getAlign(), "left");
                //2го помещаем в свободную ячейку. то есть если первый лег в правую часть, то 2го кладем в левую.
                if (pane.getLeftComponent()!=null) {
                    placeChild(childComponent2, getChildren().get(1).getAlign(), "right");
                }
                else {
                    placeChild(childComponent2, getChildren().get(1).getAlign(), "left");
                }
            }
            else {
                logger.warn("scroll must contain 2 children");
            }
        }
        else {
            logger.fatal("Expected component of type "+JSplitPane.class+" but recieved: "+getContent().getClass());
        }
    }

    private void placeChild(JComponent child, String position, String defaultPosition) {
        JSplitPane pane = (JSplitPane) getComponent();
        if (position==null) {
            position = defaultPosition;
        }

        if ("left".equals(position)) {
            pane.setLeftComponent(child);
        }
        else if ("right".equals(position)) {
            pane.setRightComponent(child);
        }
        else if ("top".equals(position)) {
            pane.setTopComponent(child);
        }
        else if ("bottom".equals(position)) {
            pane.setBottomComponent(child);
        }
        else {
            logger.warn("Unknown position: "+position);
        }
    }

    @Override
    public void applyAttributes(JComponent component) {
        if (component instanceof JSplitPane) {

            JSplitPane pane = (JSplitPane) component;
            pane.setOrientation(orientation);
            pane.setDividerSize(dividerWidth);
            if (dividerPercentLocation!=null) {
                pane.setDividerLocation(dividerPercentLocation);
            }
            else if (dividerLocation>=0) {
                pane.setDividerLocation(dividerLocation);
            }
        }
        else {
            logger.fatal("Expected component of type "+JSplitPane.class+" but recieved: "+component.getClass());
        }
        super.applyAttributes(component);
    }

    @Override
    public void setAttribute(String name, String value) {
        if (ORIENTATION_ATTRIBUTE.equals(name)) {
            if ("vertical".equals(value)) {
                orientation = JSplitPane.VERTICAL_SPLIT;
            }
            else if ("horizontal".equals(value)) {
                orientation = JSplitPane.HORIZONTAL_SPLIT;
            }
            else if (StringUtils.isEmpty(value)) {
                orientation = JSplitPane.HORIZONTAL_SPLIT;
            }
            else {
                logger.warn("Unknown orientation: "+value);
            }
        }
        else if (DIVIDER_SIZE_ATTRIBUTE.equals(name)) {
            dividerWidth = new Integer(value);
        }
        else if (DIVIDER_POSITION_ATTRIBUTE.equals(name)) {
            final int index = value.indexOf("%");
            if (index !=-1) {
                String val = value.substring(0, index);
                dividerPercentLocation = new Integer(val)/100d;
            }
            else {
                dividerLocation = new Integer(value);
            }
        }
        else {
            super.setAttribute(name, value);
        }
    }
}
