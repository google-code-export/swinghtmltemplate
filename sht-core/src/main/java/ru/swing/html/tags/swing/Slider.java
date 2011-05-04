package ru.swing.html.tags.swing;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import ru.swing.html.ELUtils;
import ru.swing.html.Utils;
import ru.swing.html.tags.Tag;
import ru.swing.html.tags.event.ChangeDelegator;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.lang.reflect.Method;

/**
 * JSlider
 */
public class Slider extends Tag {

    private Log logger = LogFactory.getLog(getClass());
    private String max;
    private String min;
    private String value;
    private String onChange;
    private String snap;
    private String majorSpace;
    private String minorSpace;

    private ChangeDelegator changeDelegator;


    @Override
    public JComponent createComponent() {
        return new JSlider();
    }


    @Override
    public void applyAttributes(JComponent component) {
        JSlider slider = (JSlider) component;

        //orientaion
        if (StringUtils.isNotEmpty(getType())) {
            int type = SwingConstants.HORIZONTAL;
            if ("vertical".equals(getType())) {
                type = SwingConstants.VERTICAL;
            }
            slider.setOrientation(type);
        }

        //maximum
        if (StringUtils.isNotEmpty(getMax())) {
            String max = ELUtils.parseStringValue(getMax(), getModelElements());
            int maxV = (Integer) Utils.convertStringToObject(max, Integer.class);
            slider.setMaximum(maxV);
        }

        //minimum
        if (StringUtils.isNotEmpty(getMin())) {
            String min = ELUtils.parseStringValue(getMin(), getModelElements());
            int minV = (Integer) Utils.convertStringToObject(min, Integer.class);
            slider.setMinimum(minV);
        }

        //snapToTicks
        if (StringUtils.isNotEmpty(getSnap())) {
            String val = ELUtils.parseStringValue(getSnap(), getModelElements());
            Boolean snap = (Boolean) Utils.convertStringToObject(val, Boolean.class);
            slider.setSnapToTicks(snap);
        }

        //majorTickSpacing
        if (StringUtils.isNotEmpty(getMajorSpace())) {
            String val = ELUtils.parseStringValue(getMajorSpace(), getModelElements());
            int v = (Integer) Utils.convertStringToObject(val, Integer.class);
            slider.setMajorTickSpacing(v);
        }

        //minorTickSpacing
        if (StringUtils.isNotEmpty(getMinorSpace())) {
            String val = ELUtils.parseStringValue(getMinorSpace(), getModelElements());
            int v = (Integer) Utils.convertStringToObject(val, Integer.class);
            slider.setMinorTickSpacing(v);
        }

        //bind value
        if (StringUtils.isNotEmpty(getValue())) {
            BeanProperty prop = BeanProperty.create("value");
            bind(getValue(), slider, prop, AutoBinding.UpdateStrategy.READ_WRITE);
        }

        //onchange listener
        if (StringUtils.isNotEmpty(getOnChange())) {

            Object controller = getModel().getController();
            Method method = Utils.findActionMethod(controller.getClass(), getOnChange(), ChangeEvent.class);
            //если метод нашелся, то добавляем к компоненту слушатель, который вызывает метод.
            if (method!=null) {
                //добавляем слушатель, который вызывает метод
                if (changeDelegator !=null) {
                    slider.removeChangeListener(changeDelegator);
                }
                changeDelegator = new ChangeDelegator(controller, method);
            }
            else {
                logger.warn(toString()+ ": can't find method " + getOnChange() + " in class " +controller.getClass().getName());
            }


            slider.addChangeListener(changeDelegator);

        }

    }

    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);
        if ("max".equals(name)) {
            setMax(value);
        }
        else if ("min".equals(name)) {
            setMin(value);
        }
        else if ("value".equals(name)) {
            setValue(value);
        }
        else if ("onchange".equals(name)) {
            setOnChange(value);
        }
        else if ("snap".equals(name)) {
            setSnap(value);
        }
        else if ("majorspace".equals(name)) {
            setMajorSpace(value);
        }
        else if ("minorspace".equals(name)) {
            setMinorSpace(value);
        }
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOnChange() {
        return onChange;
    }

    public void setOnChange(String onChange) {
        this.onChange = onChange;
    }

    public String getMajorSpace() {
        return majorSpace;
    }

    public void setMajorSpace(String majorSpace) {
        this.majorSpace = majorSpace;
    }

    public String getMinorSpace() {
        return minorSpace;
    }

    public void setMinorSpace(String minorSpace) {
        this.minorSpace = minorSpace;
    }

    public String getSnap() {
        return snap;
    }

    public void setSnap(String snap) {
        this.snap = snap;
    }
}
