package ru.swing.html.tags.swing;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import ru.swing.html.Utils;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.lang.Class;
import java.lang.ClassNotFoundException;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import java.util.Map;

/**
 *  <p>Tag is converted to the `javax.swing.JTable`.</p>
 *
 *  <p>Use this tag to iterate through list of simular items (with binding to it). Items will be displayed in table,
 *  one item per row.
 *  Data will be retrieved with `value` attribute value. It is EL, pointing to `java.util.List` model's property.
 *  Use `org.jdesktop.observablecollections.ObservableCollections.observableList()` to automatically update table's
 *  model on adding/removing element to model's list.</p>
 *
 *  <p>To specify columns for a table, use `&lt;c:column/>` tag. The `value` attribute of `column` tag must resolve item's
 *  property to display in this column.</p>
 *
 * 
 *  <h2>Example:</h2>
 *
 *  domain object:
 *  <pre>
 *  public class Person {
 *      private String name;
 *      private String email;
 *      ... //getters and setters with propertyChangeSupport usage
 *  }
 *  </pre>
 *
 *
 *  model:
 *  <pre>
 *  public class Model {
 *      private List<Person> persons;
 *      private Person selectedPerson;
 *
 *      public Model() {
 *          persons = org.jdesktop.observablecollections.ObservableCollections.observableList(new ArrayList());
 *          //fill some data
 *          persons.add(new Person());
 *          persons.add(new Person());
 *          persons.add(new Person());
 *      }
 *
 *      ...//getters and setters
 *  }
 *  </pre>
 *
 *  controller:
 *  <pre>
 *  public class FormPanel extends JPanel {
 *
 *      &#64;ModelAttribute("model")
 *      private Model model = new Model();
 *
 *      ...
 *  }
 *  </pre>
 *
 *  form:
 *  <pre>
 *  &lt;c:dataTable value="${model.persons}" selectedElement="${model.selectedPerson}">
 *      &lt;c:column value="name" title="Name">
 *      &lt;c:column value="email">
 *  &lt;/c:dataTable>
 *  </pre>
 *
 *  <p>in this example we create table with 2 columns: name and email. Both resolve corresponding properties of
 *  `Person` class.
 *  Table will have 3 rows (as we added 3 persons in `Model` constructor). Take a look that all table's cells are binded
 *  to model values, so changing cell's value will update model's value.</p>
 *
 *
 *  <p>To change selection model of the table, use `selectionType` attribute. Possible values are:</p>
 *  <ul>
 *   <li>single - the same as ListSelectionModel.SINGLE_SELECTION
 *   <li>multiple` - the same as ListSelectionModel.SINGLE_INTERVAL_SELECTION
 *   <li>custom` - the same as ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
 *  </ul>
 *
 *  <p>You can track selection with `selectedElement` attribute (`selectedElements` to track multiple selection).
 *  The value of this attribute is EL pointing to model's property. This is read-only binding, so changing model's
 *  property value won't change selection, but changing selection will update property's value.</p>
 *
 *
 *  <p>Use `autoresize` attribute to specify table's column resizing strategy (JTable.setAutoResizeMode()).
 *  Possible values are:</p>
 *  <ul>
 *   <li>off - the same as JTable.AUTO_RESIZE_OFF
 *   <li>all - the same as JTable.AUTO_RESIZE_ALL_COLUMNS
 *   <li>last - the same as JTable.AUTO_RESIZE_LAST_COLUMN
 *   <li>next - the same as JTable.AUTO_RESIZE_NEXT_COLUMN
 *   <li>auto - the same as JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS
 *  </ul>
 */
public class DataTable extends Tag {

    public static final String VALUE_ATTRIBUTE = "value";
    private Log logger = LogFactory.getLog(getClass());
    private String value;
    private String selectedElement;
    private String selectedElements;
    private String selectionType;
    private String autoresize;

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

        //get items for table model
        ELProperty beanProperty = ELProperty.create(getValue());
        Object propertyValue = beanProperty.getValue(getModel().getModelElements());
        if (!(propertyValue instanceof java.util.List)) {
            logger.warn(toString()+": dataTable must be binded to the property of type "+List.class.getName());
            return;
        }
        List<Object> values = (List<Object>) propertyValue;

        JTableBinding binding = SwingBindings.createJTableBinding(AutoBinding.UpdateStrategy.READ_WRITE, values, (JTable) getComponent());
        logger.trace(toString()+": created binding on property '"+getValue()+"'");



        //bind each child <column> to corresponding prop
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
                //el-editor has more priority than editorClass
                if (StringUtils.isNotEmpty(col.getEditor())) {
                    ELProperty rendererProperty = ELProperty.create(col.getEditor());
                    Object editor = rendererProperty.getValue(getModel().getModelElements());
                    if (editor instanceof TableCellEditor) {
                        cb.setEditor((TableCellEditor) editor);
                        logger.trace(toString()+": set column editor: '"+col.getEditor()+"'");
                    }
                    else if (editor == null) {
                        logger.warn(toString()+ ": can't set column renderer. Object '"+col.getEditor() + " is null");
                    }
                    else {
                        logger.warn(toString()+ ": can't set column renderer. Object '"+col.getEditor() + " is not instance of "+TableCellEditor.class.getName());
                    }
                }
                else if (StringUtils.isNotEmpty(col.getEditorClass())) {
                    try {
                        Class columnClass = Utils.convertStringToClass(col.getEditorClass());
                        if (TableCellEditor.class.isAssignableFrom(columnClass)) {
                            try {
                                TableCellEditor instance = (TableCellEditor) columnClass.newInstance();
                                cb.setEditor(instance);
                                logger.trace(toString()+": set column editor: '"+col.getEditorClass()+"'");
                            } catch (InstantiationException e) {
                                logger.error(toString()+ ": can't create instance of editor. Class '"+col.getEditorClass(), e);
                            } catch (IllegalAccessException e) {
                                logger.error(toString()+ ": can't create instance of editor. Class '"+col.getEditorClass(), e);
                            }
                        }
                        else {
                            logger.warn(toString()+ ": can't create instance of editor. Class '"+col.getEditorClass() + " is not instance of "+TableCellEditor.class.getName());
                        }
                    } catch (ClassNotFoundException e) {
                        logger.warn(toString()+ ": can't set column type. Class '"+col.getEditorClass()+"' is not supported.");
                    }
                }


                //install renderer
                //el-renderer has more priority than rendererClass
                if (StringUtils.isNotEmpty(col.getRenderer())) {
                    ELProperty rendererProperty = ELProperty.create(col.getRenderer());
                    Object renderer = rendererProperty.getValue(getModel().getModelElements());
                    if (renderer instanceof TableCellRenderer) {
                        cb.setRenderer((TableCellRenderer) renderer);
                        logger.trace(toString()+": set column renderer: '"+col.getRenderer()+"'");
                    }
                    else if (renderer == null) {
                        logger.warn(toString()+ ": can't set column renderer. Object '"+col.getRenderer() + " is null");
                    }
                    else {
                        logger.warn(toString()+ ": can't set column renderer. Object '"+col.getRenderer() + " is not instance of "+TableCellRenderer.class.getName());
                    }
                }
                else if (StringUtils.isNotEmpty(col.getRendererClass())) {
                    try {
                        Class columnClass = Utils.convertStringToClass(col.getRendererClass());
                        if (TableCellRenderer.class.isAssignableFrom(columnClass)) {
                            try {
                                TableCellRenderer instance = (TableCellRenderer) columnClass.newInstance();
                                cb.setRenderer(instance);
                                logger.trace(toString()+": set column renderer: '"+col.getRendererClass()+"'");
                            } catch (InstantiationException e) {
                                logger.warn(toString()+ ": can't create instance of renderer. Class '"+col.getRendererClass());
                            } catch (IllegalAccessException e) {
                                logger.warn(toString()+ ": can't create instance of renderer. Class '"+col.getRendererClass());
                            }
                        }
                        else {
                            logger.warn(toString()+ ": can't create instance of renderer. Class '"+col.getRendererClass() + " is not instance of "+TableCellRenderer.class.getName());
                        }
                    } catch (ClassNotFoundException e) {
                        logger.warn(toString()+ ": can't set column type. Class '"+col.getRendererClass()+"' is not supported.");
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
    public void applyAttribute(JComponent component, String name) {

        //set selection model
        JTable table = (JTable) getComponent();

        if ("selectiontype".equals(name)) {
            if (StringUtils.isNotEmpty(getSelectionType())) {
                int selectionMode = table.getSelectionModel().getSelectionMode();
                if ("single".equals(getSelectionType())) {
                    selectionMode = ListSelectionModel.SINGLE_SELECTION;
                }
                else if ("multiple".equals(getSelectionType())) {
                    selectionMode = ListSelectionModel.SINGLE_INTERVAL_SELECTION;
                }
                else if ("custom".equals(getSelectionType())) {
                    selectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
                }
                table.getSelectionModel().setSelectionMode(selectionMode);
                logger.trace(toString()+": set selection mode: "+getSelectionType());
            }
        }
        else if ("selectedelement".equals(name)) {
            //bind selected row
            if (StringUtils.isNotEmpty(getSelectedElement())) {
                BeanProperty selectedElement = BeanProperty.create("selectedElement");
                getModel().bind(getSelectedElement(), getComponent(), selectedElement);
                logger.trace(toString()+": binded 'selectedElement' to "+getSelectedElement());
            }
        }
        else if ("selectedelements".equals(name)) {
            //bind selected rows
            if (StringUtils.isNotEmpty(getSelectedElements())) {
                BeanProperty selectedElement = BeanProperty.create("selectedElements");
                getModel().bind(getSelectedElements(), getComponent(), selectedElement);
                logger.trace(toString()+": binded 'selectedElements' to "+getSelectedElements());
            }
        }
        else if ("autoresize".equals(name)) {
            //set column autoresize mode
            if (StringUtils.isNotEmpty(getAutoresize())) {
                int mode = table.getAutoResizeMode();
                if ("off".equals(getAutoresize())) {
                    mode = JTable.AUTO_RESIZE_OFF;
                }
                else if ("all".equals(getAutoresize())) {
                    mode = JTable.AUTO_RESIZE_ALL_COLUMNS;
                }
                else if ("last".equals(getAutoresize())) {
                    mode = JTable.AUTO_RESIZE_LAST_COLUMN;
                }
                else if ("next".equals(getAutoresize())) {
                    mode = JTable.AUTO_RESIZE_NEXT_COLUMN;
                }
                else if ("auto".equals(getAutoresize())) {
                    mode = JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS;
                }
                logger.trace(toString()+": set autoResizeMode to "+getAutoresize());
                table.setAutoResizeMode(mode);
            }

        }
        else {
            super.applyAttribute(component, name);
        }
    }

    @Override
    public void afterComponentsConverted() {
        //set columns width
        JTable table = (JTable) getComponent();
        TableColumnModel model = table.getColumnModel();
        int i = 0;
        for (Tag childTag : getChildren()) {
            if (childTag instanceof Column) {

                Column col = (Column) childTag;
                if (StringUtils.isNotEmpty(col.getWidth())) {
                    model.getColumn(i).setWidth(Utils.convertStringToObject(col.getWidth(), Integer.class));
                    model.getColumn(i).setPreferredWidth(Utils.convertStringToObject(col.getWidth(), Integer.class));
                }
                i++;
            }

        }
    }

    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);
        if (VALUE_ATTRIBUTE.equals(name)) {
            setValue(value);
        }
        else if ("selectedelement".equals(name)) {
            setSelectedElement(value);
        }
        else if ("selectedelements".equals(name)) {
            setSelectedElements(value);
        }
        else if ("selectiontype".equals(name)) {
            setSelectionType(value);
        }
        else if ("autoresize".equals(name)) {
            setAutoresize(value);
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSelectedElement() {
        return selectedElement;
    }

    public void setSelectedElement(String selectedElement) {
        this.selectedElement = selectedElement;
    }

    public String getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(String selectionType) {
        this.selectionType = selectionType;
    }

    public String getSelectedElements() {
        return selectedElements;
    }

    public void setSelectedElements(String selectedElements) {
        this.selectedElements = selectedElements;
    }

    public String getAutoresize() {
        return autoresize;
    }

    public void setAutoresize(String autoresize) {
        this.autoresize = autoresize;
    }
}
