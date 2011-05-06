package ru.swing.html.tags.swing;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.swingbinding.JComboBoxBinding;
import org.jdesktop.swingbinding.JListBinding;
import org.jdesktop.swingbinding.SwingBindings;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Tag;
import ru.swing.html.tags.swing.SelectItems;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.lang.*;
import java.lang.Object;
import java.util.Map;

/**
 * JList
 */
public class List extends Tag {

    private Log logger = LogFactory.getLog(getClass());
    private String selectedElement;
    private String selectedElements;
    private String renderer;
    private String rowsPerColumn;

    @Override
    public JComponent createComponent() {
        JList jList = new JList();
        setComponent(jList);
        return jList;
    }

    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {

        JList list = (JList) getComponent();

        //bind selected row
        if (StringUtils.isNotEmpty(getSelectedElement())) {
            BeanProperty selectedElement = BeanProperty.create("selectedElement");
            getModel().bind(getSelectedElement(), getComponent(), selectedElement);
            logger.trace(toString()+": binded 'selectedElement' to "+getSelectedElement());
        }


        //bind selected rows
        if (StringUtils.isNotEmpty(getSelectedElements())) {
            BeanProperty selectedElement = BeanProperty.create("selectedElements");
            getModel().bind(getSelectedElement(), getComponent(), selectedElement);
            logger.trace(toString()+": binded 'selectedElements' to "+getSelectedElement());
        }



        for (Tag child : getChildren()) {

            if (child instanceof SelectItems) {
                String modelEL = ((SelectItems)child).getValue();
                //get items for list model
                ELProperty beanProperty = ELProperty.create(modelEL);
                java.lang.Object propertyValue = beanProperty.getValue(getModel().getModelElements());
                if (!(propertyValue instanceof java.util.List)) {
                    logger.warn(toString()+": selectItems must be binded to the property of type "+ java.util.List.class.getName());
                    return;
                }
                java.util.List<java.lang.Object> values = (java.util.List<Object>) propertyValue;

                JListBinding binding = SwingBindings.createJListBinding(AutoBinding.UpdateStrategy.READ_WRITE, values, (JList) getComponent());
                binding.bind();
                logger.trace(toString()+": created binding on property '"+modelEL+"'");


            }
        }


        //install renderer
        String rendererAttr = getRenderer();
        if (StringUtils.isNotEmpty(rendererAttr)) {
            ELProperty rendererProperty = ELProperty.create(rendererAttr);
            Object editorVal = rendererProperty.getValue(getModel().getModelElements());
            if (editorVal==null) {
                logger.warn(toString()+": can't set renderer, '"+rendererAttr+"' resolved to null");
            }
            else if (editorVal instanceof ListCellRenderer) {
                list.setCellRenderer((ListCellRenderer) editorVal);
                logger.trace(toString()+": set renderer: '"+rendererAttr+"'");
            }
            else {
                logger.warn(toString()+": can't set renderer, '"+rendererAttr+"' is not instance of "+ListCellRenderer.class);
            }

        }


    }

    @Override
    public void applyAttribute(JComponent component, String name) {
        JList jlist = (JList) component;

        //set layout orientation
        if (StringUtils.isNotEmpty(getType())) {
            int type = JList.VERTICAL;
            if ("vertical-wrap".equals(getType())) {
                type = JList.VERTICAL_WRAP;
            }
            else if ("horizontal-wrap".equals(getType())) {
                type = JList.HORIZONTAL_WRAP;
            }
            jlist.setLayoutOrientation(type);
            jlist.setVisibleRowCount(0);
        }

        if (StringUtils.isNotEmpty(getRowsPerColumn())) {

            if (StringUtils.isNumeric(getRowsPerColumn())) {
                int rows = Integer.parseInt(getRowsPerColumn());
                jlist.setVisibleRowCount(rows);
            }
            else {
                logger.warn(toString()+": can't parse 'rowsPerColumn' attribute value, it is not numeric");
            }
        }
        else {
            super.applyAttribute(component, name);

        }
    }

    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);
        if ("selectedelement".equals(name)) {
            setSelectedElement(value);
        }
        else if ("selectedelements".equals(name)) {
            setSelectedElements(value);
        }
        else if ("renderer".equals(name)) {
            setRenderer(value);
        }
        else if ("rowsPerColumn".equalsIgnoreCase(name)) {
            setRowsPerColumn(value);
        }
    }

    public String getSelectedElements() {
        return selectedElements;
    }

    public void setSelectedElements(String selectedElements) {
        this.selectedElements = selectedElements;
    }

    public String getSelectedElement() {
        return selectedElement;
    }

    public void setSelectedElement(String selectedElement) {
        this.selectedElement = selectedElement;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public String getRowsPerColumn() {
        return rowsPerColumn;
    }

    public void setRowsPerColumn(String rowsPerColumn) {
        this.rowsPerColumn = rowsPerColumn;
    }
}
