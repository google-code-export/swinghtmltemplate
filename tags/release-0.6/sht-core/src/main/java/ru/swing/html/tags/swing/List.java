package ru.swing.html.tags.swing;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.swingbinding.JListBinding;
import org.jdesktop.swingbinding.SwingBindings;
import ru.swing.html.configuration.MethodInvoker;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Tag;
import ru.swing.html.tags.event.ListSelectionDelegator;

import javax.swing.*;
import java.util.Map;

/**
 *  <p>Tag is converted to the `javax.swing.JList`.</p>
 *
 *  <p>Currently it supports only one child tag: an `selectItems` tag, which holds the EL for list model. If it doesn't present,
 *  no model is installed.</p>
 *
 *  <p>List's model is evaluated with jsr-255 binding. Source bean property must be of type `java.util.List`. Use
 *  `org.jdesktop.observablecollections.ObservableCollections.observableList()` to create observable list, so adding/removing
 *  elements to/from collection will add/remove elements from JList model.</p>
 *
 *  <p>You can bind selected list element with `selectedElement` attribute. `selectedElements` can be used to bind
 *  many selected elements. Currently, due to limitations of better beans binding, these are readonly bindings (changing
 *  model won't update JList selection).</p>
 *
 *  <p>You can set renderer for the list with `renderer` attribute. This must be EL, pointing to the `javax.swing.ListCellRenderer`
 *  instance.</p>
 *
 *  <p>You can set the number of rows per column with `rowsPerColumn` attribute. It is used when list must be with several columns.
 *  Use the `type` attribute to set rendering type. Possible values for `type` are:</p>
 *  <ul>
 *   <li>vertical
 *   <li>horizontal-wrap
 *   <li>vertical-wrap
 *  </ul>
 *
 *  <pre>
 *  //controller
 *  public class MyPanel {
 *
 *      &#64;ModelElement("model")
 *      private MyPanelModel model;
 *
 *      &#64;ModelElement("customRenderer")
 *      private CustomRenderer projectTypeRenderer = new CustomRenderer();
 *
 *     ...
 *
 *     public void foo() {
 *        items.add("Line 1");
 *        items.add("Line 2");
 *     }
 *
 *
 *  }
 *
 *
 *  //model
 *  public class MyPanelModel {
 *     private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
 *     private List items = ObservableCollections.observableList(new ArrayList());
 *     private String selected;
 *     //getters and setters with PropertyChangeSupport firing
 *
 *     ...
 *
 *  }
 *
 *  //renderer
 *  public class CustomRenderer extends DefaultListCellRenderer implements ListCellRenderer {
 *     ...
 *  }
 *  </pre>
 *
 *  <pre>
 *  &lt;c:list selectedElement="${model.selected}" renderer="${customRenderer}">
 *     &lt;c:selectItems value="${model.items}"/>
 *  &lt;/c:list>
 *  </pre?
 *
 * Tag supports 'onchange' event, invoked when current item is changed in combobox. Attribute value
 * is method name. Method can have no arguments or have one argument of type javax.swing.event.ListSelectionEvent.
 */
public class List extends Tag {

    private Log logger = LogFactory.getLog(getClass());
    private String selectedElement;
    private String selectedElements;
    private String renderer;
    private String onchange;
    private String rowsPerColumn;
    private ListSelectionDelegator listSelectionDelegator;

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
            getModel().bind(getSelectedElements(), getComponent(), selectedElement);
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
        if ("type".equals(name)) {
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
        }
        else if ("rowspercolumn".equalsIgnoreCase(name)) {
            if (StringUtils.isNotEmpty(getRowsPerColumn())) {

                if (StringUtils.isNumeric(getRowsPerColumn())) {
                    int rows = Integer.parseInt(getRowsPerColumn());
                    jlist.setVisibleRowCount(rows);
                }
                else {
                    logger.warn(toString()+": can't parse 'rowsPerColumn' attribute value, it is not numeric");
                }
            }
        }
        else if ("onchange".equals(name)) {
            //install model selection listener
            final String onchangeMethod = getOnchange();
            if (StringUtils.isNotEmpty(onchangeMethod)) {

                MethodInvoker invoker = getModel().getConfiguration().getMethodResolverService().resolveMethod(onchangeMethod, this);
                //if invoker is found
                if (invoker!=null) {
                    //create delegator
                    if (listSelectionDelegator !=null) {
                        jlist.getSelectionModel().removeListSelectionListener(listSelectionDelegator);
                    }
                    listSelectionDelegator = new ListSelectionDelegator(invoker);
                    jlist.getSelectionModel().addListSelectionListener(listSelectionDelegator);
                }
                else {
                    logger.warn(toString()+ ": can't find method invoker for '" + onchangeMethod + "'");
                }


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
        else if ("rowspercolumn".equalsIgnoreCase(name)) {
            setRowsPerColumn(value);
        }
        else if ("onchange".equals(name)) {
            setOnchange(value);
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

    public String getOnchange() {
        return onchange;
    }

    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }
}
