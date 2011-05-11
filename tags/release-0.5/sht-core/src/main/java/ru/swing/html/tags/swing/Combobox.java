package ru.swing.html.tags.swing;

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
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.lang.*;
import java.util.List;
import java.util.Map;

/**
 * <p>Tag is converted to the javax.swing.JComboBox</p>
 *
 * <p>Currently it supports only one child tag: an "selectItems" tag, which holds the EL for list model. If it doesn't present,
 * no model is installed.</p>
 *
 * <p>JComboBox's model is evaluated with jsr-255 binding. Source bean property must be of type `java.util.List`. Use
 * `org.jdesktop.observablecollections.ObservableCollections.observableList()` to create observable list, so adding/removing
 * elements to/from collection will add/remove elements from JComboBox model.</p>
 *
 * <p>You can bind selected combobox element with `selectedElement` attribute. Currently, due to limitations of better beans
 * binding, this is readonly bindings (changing model won't update JList selection).</p>
 *
 * <pre>
 * &lt;c:combobox align='center' selectedElement="${model.runtime}">
 *    &lt;c:selectItems value="${runtimeValues}"/>
 * &lt;/c:combobox>
 * </pre>
 *
 * <pre>
 * public class CreateProjectForm extends JPanel {
 *
 *    ...
 *
 *    &#64;ModelElement("runtimeValues")
 *    private List<String> runtimeValues = Arrays.asList("Apache Tomcat v5.5", "Apache Tomcat v6.0");
 *
 *    &#64;ModelElement("model")
 *    private Model model = new Model();
 *
 *    ...
 * }
 * </pre>
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
                if (propertyValue == null) {
                    logger.warn(toString()+": selectItems binding target '"+modelEL+"' is null");
                }
                else if (!(propertyValue instanceof java.util.List)) {
                    logger.warn(toString()+": selectItems must be binded to the property of type "+List.class.getName());
                    return;
                }
                else {
                    List<java.lang.Object> values = (List<java.lang.Object>) propertyValue;

                    JComboBoxBinding binding = SwingBindings.createJComboBoxBinding(AutoBinding.UpdateStrategy.READ_WRITE, values, (JComboBox) getComponent());
                    binding.bind();
                    logger.trace(toString()+": created binding on property '"+modelEL+"'");
                }


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
