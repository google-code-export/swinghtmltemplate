package ru.swing.html.tags.swing;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import ru.swing.html.ELUtils;
import ru.swing.html.Utils;
import ru.swing.html.configuration.MethodInvoker;
import ru.swing.html.tags.Tag;
import ru.swing.html.tags.event.ChangeDelegator;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.lang.reflect.Method;

/**
 * <p>
 *  This tag produces JSlider.
 * </p>
 * <p>
 *     Supported attributes:
 *     <ul>
 *         <li>max - maximum slider's value</li>
 *         <li>min - minimum slider's value</li>
 *         <li>snap - same as JSlider#setSnapToTicks</li>
 *         <li>majorSpace - same as JSlider#setMajorTickSpacing</li>
 *         <li>minorSpace - same as JSlider#setMinorTickSpacing</li>
 *
 * </p>
 * <p>
 *     The slider's value can be be binded to model element using 'value' attribute.
 * </p>
 * <p>
 *     This tag supports 'onchange' event, which is invoked when slider's value is changed. The value of
 *     an attribute is controller's method name. Method must take no arguments or one argument of type ChangeEvent.
 * </p>
 *
 * @see JSlider
 */
public class Slider extends Tag {

    public static final String MAXIMUM_ATTRIBUTE = "max";
    public static final String MINIMUM_ATTRIBUTE = "min";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String SNAP_TO_TICKS_ATTRIBUTE = "snap";
    public static final String MAJOR_SPACE_ATTRIBUTE = "majorspace";
    public static final String MINOR_SPACE_ATTRIBUTE = "minorspace";
    public static final String ONCHANGE_ATTRIBUTE = "onchange";
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
    public void applyAttribute(JComponent component, String name) {
        JSlider slider = (JSlider) component;

        if (TYPE_ATTRIBUTE.equals(name)) {
            //orientaion
            if (StringUtils.isNotEmpty(getType())) {
                int type = SwingConstants.HORIZONTAL;
                if ("vertical".equals(getType())) {
                    type = SwingConstants.VERTICAL;
                }
                slider.setOrientation(type);
            }
        }
        else if (MAXIMUM_ATTRIBUTE.equals(name)) {
            //maximum
            if (StringUtils.isNotEmpty(getMax())) {
                String max = ELUtils.parseStringValue(getMax(), getModelElements());
                int maxV = (Integer) Utils.convertStringToObject(max, Integer.class);
                slider.setMaximum(maxV);
            }
        }
        else if (MINIMUM_ATTRIBUTE.equals(name)) {
            //minimum
            if (StringUtils.isNotEmpty(getMin())) {
                String min = ELUtils.parseStringValue(getMin(), getModelElements());
                int minV = (Integer) Utils.convertStringToObject(min, Integer.class);
                slider.setMinimum(minV);
            }
        }
        else if (SNAP_TO_TICKS_ATTRIBUTE.equals(name)) {
            //snapToTicks
            if (StringUtils.isNotEmpty(getSnap())) {
                String val = ELUtils.parseStringValue(getSnap(), getModelElements());
                Boolean snap = (Boolean) Utils.convertStringToObject(val, Boolean.class);
                slider.setSnapToTicks(snap);
            }
        }
        else if (MAJOR_SPACE_ATTRIBUTE.equals(name)) {
            //majorTickSpacing
            if (StringUtils.isNotEmpty(getMajorSpace())) {
                String val = ELUtils.parseStringValue(getMajorSpace(), getModelElements());
                int v = (Integer) Utils.convertStringToObject(val, Integer.class);
                slider.setMajorTickSpacing(v);
            }
        }
        else if (MINOR_SPACE_ATTRIBUTE.equals(name)) {
            //minorTickSpacing
            if (StringUtils.isNotEmpty(getMinorSpace())) {
                String val = ELUtils.parseStringValue(getMinorSpace(), getModelElements());
                int v = (Integer) Utils.convertStringToObject(val, Integer.class);
                slider.setMinorTickSpacing(v);
            }
        }
        else if (VALUE_ATTRIBUTE.equals(name)) {
            //bind value
            if (StringUtils.isNotEmpty(getValue())) {
                BeanProperty prop = BeanProperty.create("value");
                bind(getValue(), slider, prop, AutoBinding.UpdateStrategy.READ_WRITE);
            }
        }
        else if (ONCHANGE_ATTRIBUTE.equals(name)) {
            //onchange listener
            if (StringUtils.isNotEmpty(getOnChange())) {

                MethodInvoker invoker = getModel().getConfiguration().getMethodResolverService().resolveMethod(getOnChange(), this);
                //if invoker is found
                if (invoker!=null) {
                    //добавляем слушатель, который вызывает метод
                    if (changeDelegator !=null) {
                        slider.removeChangeListener(changeDelegator);
                    }
                    changeDelegator = new ChangeDelegator(invoker);
                }
                else {
                    logger.warn(toString()+ ": can't find method method invoker for '" + getOnChange() + "'");
                }


                slider.addChangeListener(changeDelegator);

            }
        }
        else {
            super.applyAttribute(component, name);
        }

    }

    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);
        if (MAXIMUM_ATTRIBUTE.equals(name)) {
            setMax(value);
        }
        else if (MINIMUM_ATTRIBUTE.equals(name)) {
            setMin(value);
        }
        else if (VALUE_ATTRIBUTE.equals(name)) {
            setValue(value);
        }
        else if (ONCHANGE_ATTRIBUTE.equals(name)) {
            setOnChange(value);
        }
        else if (SNAP_TO_TICKS_ATTRIBUTE.equals(name)) {
            setSnap(value);
        }
        else if (MAJOR_SPACE_ATTRIBUTE.equals(name)) {
            setMajorSpace(value);
        }
        else if (MINOR_SPACE_ATTRIBUTE.equals(name)) {
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
