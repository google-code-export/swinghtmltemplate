package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import ru.swing.html.DomConverter;
import ru.swing.html.Utils;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.layout.LayoutManagerSupport;
import ru.swing.html.layout.LayoutManagerSupportFactory;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.lang.*;
import java.lang.Object;
import java.util.List;
import java.util.Map;

/**
 * Represents JTable. Can contain childs of type Column.
 * Use 'value' attribute to bind table to model. Model must be of type java.util.List.
 * Use
 * <pre>ObservableCollections.observableList();</pre> to support adding and removing elements.
 */
public class DataTable extends Tag {

    private Log logger = LogFactory.getLog(getClass());
    private String value;

    @Override
    public JComponent createComponent() {
        JTable c = new JTable();
        setComponent(c);
        return c;
    }


    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {
        if (StringUtils.isEmpty(getValue())) {
            logger.warn(toString()+": 'value' attribute is not set for dataTable.");
            return;
        }

        ELProperty beanProperty = ELProperty.create(getValue());
        Object propertyValue = beanProperty.getValue(getModel().getModelElements());
        if (!(propertyValue instanceof java.util.List)) {
            logger.warn(toString()+": dataTable must be binded to the property of type java.util.List.");
            return;
        }
        List<Object> values = (List<Object>) propertyValue;
        
        JTableBinding binding = SwingBindings.createJTableBinding(AutoBinding.UpdateStrategy.READ_WRITE, values, (JTable) getComponent());
        logger.trace(toString()+": created binding on property '"+getValue()+"'");

        for (Tag childTag : getChildren()) {

            if (childTag instanceof Column) {

                Column col = (Column) childTag;
                String value = col.getValue();
                ELProperty<Map<String, Object>, String> colProp = ELProperty.create(value);

                JTableBinding.ColumnBinding cb = binding.addColumnBinding(colProp);
                logger.trace(toString()+": added column binding on property '"+value+"'");

                cb.setColumnName(col.getTitle());
                cb.setEditable(col.isEditable());


                //set column class
                String type = col.getType();
                if (StringUtils.isNotEmpty(type)) {
                    try {
                        Class columnClass = Utils.convertStringToClass(type);
                        cb.setColumnClass(columnClass);
                    } catch (ClassNotFoundException e) {
                        logger.warn(toString()+ ": can't set column type. Class '"+type+"' is not supported.");
                    }
                }

                //install editor
                if (StringUtils.isNotEmpty(col.getEditor())) {
                    try {
                        Class columnClass = Utils.convertStringToClass(col.getEditor());
                        if (TableCellEditor.class.isAssignableFrom(columnClass)) {
                            try {
                                TableCellEditor instance = (TableCellEditor) columnClass.newInstance();
                                cb.setEditor(instance);
                                logger.trace(toString()+": set column editor: '"+col.getEditor()+"'");
                            } catch (InstantiationException e) {
                                logger.error(toString()+ ": can't create instance of editor. Class '"+col.getEditor(), e);
                            } catch (IllegalAccessException e) {
                                logger.error(toString()+ ": can't create instance of editor. Class '"+col.getEditor(), e);
                            }
                        }
                        else {
                            logger.warn(toString()+ ": can't create instance of editor. Class '"+col.getEditor() + " is not instance of "+TableCellEditor.class.getName());
                        }
                    } catch (ClassNotFoundException e) {
                        logger.warn(toString()+ ": can't set column type. Class '"+type+"' is not supported.");
                    }
                }


                //install renderer
                if (StringUtils.isNotEmpty(col.getRenderer())) {
                    try {
                        Class columnClass = Utils.convertStringToClass(col.getRenderer());
                        if (TableCellRenderer.class.isAssignableFrom(columnClass)) {
                            try {
                                TableCellRenderer instance = (TableCellRenderer) columnClass.newInstance();
                                cb.setRenderer(instance);
                                logger.trace(toString()+": set column renderer: '"+col.getRenderer()+"'");
                            } catch (InstantiationException e) {
                                logger.warn(toString()+ ": can't create instance of renderer. Class '"+col.getEditor());
                            } catch (IllegalAccessException e) {
                                logger.warn(toString()+ ": can't create instance of renderer. Class '"+col.getEditor());
                            }
                        }
                        else {
                            logger.warn(toString()+ ": can't create instance of editor. Class '"+col.getRenderer() + " is not instance of "+TableCellRenderer.class.getName());
                        }
                    } catch (ClassNotFoundException e) {
                        logger.warn(toString()+ ": can't set column type. Class '"+type+"' is not supported.");
                    }
                }


            }
            else {
                logger.warn(toString()+": child "+childTag+" is not supported");
            }



        }
        binding.bind();


    }


    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);
        if ("value".equals(name)) {
            setValue(value);
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }



}
