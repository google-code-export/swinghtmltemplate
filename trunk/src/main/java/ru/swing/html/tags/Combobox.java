package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.swingbinding.JComboBoxBinding;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import ru.swing.html.css.SelectorGroup;

import javax.swing.*;
import java.lang.*;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 14.04.11
 * Time: 11:02
 */
public class Combobox extends Tag {

    private Log logger = LogFactory.getLog(getClass());
    private String selectedElement;

    @Override
    public JComponent createComponent() {
        JComboBox jList = new JComboBox();
        setComponent(jList);
        return jList;
    }

    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {

        //bind selected row
        if (StringUtils.isNotEmpty(getSelectedElement())) {
            BeanProperty selectedElement = BeanProperty.create("selectedItem");
            getModel().bind(getSelectedElement(), getComponent(), selectedElement);
            logger.trace(toString()+": binded 'selectedItem' to "+getSelectedElement());
        }



        for (Tag child : getChildren()) {

            if (child instanceof SelectItems) {
                String modelEL = ((SelectItems)child).getValue();
                //get items for table model
                ELProperty beanProperty = ELProperty.create(modelEL);
                java.lang.Object propertyValue = beanProperty.getValue(getModel().getModelElements());
                if (!(propertyValue instanceof java.util.List)) {
                    logger.warn(toString()+": selectItems must be binded to the property of type "+List.class.getName());
                    return;
                }
                List<java.lang.Object> values = (List<java.lang.Object>) propertyValue;

                JComboBoxBinding binding = SwingBindings.createJComboBoxBinding(AutoBinding.UpdateStrategy.READ_WRITE, values, (JComboBox) getComponent());
                binding.bind();
                logger.trace(toString()+": created binding on property '"+modelEL+"'");


            }
        }

    }



    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);
        if ("selectedelement".equals(name)) {
            setSelectedElement(value);
        }
    }


    public String getSelectedElement() {
        return selectedElement;
    }

    public void setSelectedElement(String selectedElement) {
        this.selectedElement = selectedElement;
    }
}
