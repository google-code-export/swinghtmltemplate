package ru.swing.html.tags.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.*;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.util.Map;

/**
 * <p>
 * Sets model element.
 * <p>
 * Source property is set with 'value' attribute, which must be EL. The value is resolved against local model elements in
 * 'before-components-convertion' phase.
 * <p>
 * Target property is set with 'var' attribute, which must be a string, describing the name of the model element.
 * This model element will be created and initialized with 'value' value.
 * 
 * 
 * <p>
 * If 'type' attribute is set to 'binding', when binding between source property and target property will
 * be created, otherwise source value is readed once and is assigned to target property.
 * 
 * <h2>Example:</h2>
 * <pre>
     &lt;html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
           xmlns:c=\"http://www.oracle.com/swing\"\n" +
           xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
     &lt;head>&lt;/head>\n" +
     &lt;body>\n" +
        &lt;ui:set var='foo' value='${show}'/>" +
        &lt;p id='p'>${foo}&lt;/p>" +
     &lt;/body>\n" +
     &lt;/html>";
 * </pre>
 * <pre>model.addModelElement("show", "111")</pre>
 * Here we set model element with the name 'foo' equals to the value of the 'show' model element ('111'). The text
 * of the label equals to the value of new model element ('111') and is assigned once, so changing model element
 * won't update label's text.
 * <p>
 * <pre>
     &lt;html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
           xmlns:c=\"http://www.oracle.com/swing\"\n" +
           xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>\n" +
     &lt;head>&lt;/head>\n" +
     &lt;body>\n" +
        &lt;ui:set var='foo' value='${show}' type='binding'/>" +
        &lt;p id='p' content='el'>${foo}&lt;/p>" +
     &lt;/body>\n" +
     &lt;/html>";
 * </pre>
 * <pre>model.addModelElement("show", "111")</pre>
 * Here we set model element with the name 'foo' and bind it to the value of the 'show' model element ('111'). The text
 * of the label equals to the value of new model element ('111') and is binded to the new 'foo' element, so updating
 * 'show' will update 'foo' which will update label's text.
 */
public class Set extends Tag {

    private Log logger = LogFactory.getLog(getClass());
    private String var;
    private String value;

    @Override
    public void handleLayout() {
    }

    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {
    }

    @Override
    public void setAttribute(String name, String value) {
        if ("var".equals(name)) {
            setVar(value);
        }
        else if ("value".equals(name)) {
            setValue(value);
        }
        super.setAttribute(name, value);
    }

    @Override
    public void beforeComponentsConvertion() {

        ELProperty sourceProp = ELProperty.create(getValue());
        
        if ("binding".equals(getType())) {
            Property targetProp = BeanProperty.create(getVar());
            Binding b = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
                    getModelElements(),
                    sourceProp,
                    getModel().getModelElements(),
                    targetProp);
            b.bind();
            logger.trace(toString()+": binded '"+getVar()+"' to '"+getValue()+"'");
        }
        else {
            if (sourceProp.isReadable(getModelElements())) {
                Object value = sourceProp.getValue(getModelElements());
                getModel().addModelElement(getVar(), value);
                logger.trace(toString()+": added model element '"+getVar()+"' from '"+getValue()+"'");
            }
            else {
                logger.trace(toString()+": cannot add model element '"+getVar()+"' from '"+getValue()+"': value is not readable");
            }
        }
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
