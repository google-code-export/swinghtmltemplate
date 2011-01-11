package ru.swing.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for binding dom-model to component. Component's fields may be annotated with @Bind annotation.
 */
public class Binder {

    private static Log logger = LogFactory.getLog(Binder.class);


    /**
     * Creates new dom-model for component and binds model to component.
     * dom-model is loaded from file, whose name equals to component's classname and which is located
     * at the same place, as component's class.
     *
     * For example, for component of class foo.Foo, file /foo/Foo.html will be used

     * @param component object, to whom dom-model will be binded
     * @return dom-model
     * @throws JDOMException
     * @throws IOException
     */
    public static DomModel bind(Object component) throws JDOMException, IOException {
        return bind(component, false);
    }


    /**
     * Creates new dom-model for component and binds model to component.
     * dom-model is loaded from file, whose name equals to component's classname and which is located
     * at the same place, as component's class.
     *
     * For example, for component of class foo.Foo, file /foo/Foo.html will be used
     *
     * If useControllerAsRoot==true and component extends javax.swing.JComponent, then component
     * will be used as root component (it will substitute &lt;body&gt;'s component),
     *
     * @param component object, to whom dom-model will be binded
     * @param useControllerAsRoot if true, use component as root component in dom-model
     * @return dom-model
     * @throws JDOMException
     * @throws IOException
     */
    public static DomModel bind(Object component, boolean useControllerAsRoot) throws JDOMException, IOException {
        //get classname
        final Class<?> cl = component.getClass();
        String className = cl.getName();
        //create file path, replace dots with '/'
        String path = "/"+className.replaceAll("\\.", "/") + ".html";
        //load model
        InputStream htmlStream = component.getClass().getResourceAsStream(path);
        if (htmlStream!=null) {
            DomModel model = DomLoader.loadModel(htmlStream);
            model.setSourcePath(path);
            return bind(component, useControllerAsRoot, model);
        }
        else {
            logger.error("Can't find html-document at "+path);
            return null;
        }
    }

    /**
     *
     * Binds component to dom-model.
     * If useControllerAsRoot==true and component extends javax.swing.JComponent, then component
     * will be used as root component (it will substitute &lt;body&gt;'s component),
     *
     * @param component object, to whom dom-model will be binded
     * @param useControllerAsRoot if true, use component as root component in dom-model
     * @param model dom-model to use
     * @return dom-model
     * @throws JDOMException
     * @throws IOException
     */
    public static DomModel bind(Object component, boolean useControllerAsRoot, DomModel model) throws JDOMException, IOException {
        Map<SelectorGroup, JComponent> substitutions = new HashMap<SelectorGroup, JComponent>();
        if (useControllerAsRoot && component instanceof JComponent) {
            SelectorGroup selector = new SelectorGroup("body");
            substitutions.put(selector, (JComponent) component);
        }
        model = bind(component, useControllerAsRoot, model, substitutions);
        return model;
    }

    /**
     * Binds component to dom-model.
     * If useControllerAsRoot==true and component extends javax.swing.JComponent, then component
     * will be used as root component (it will substitute &lt;body&gt;'s component).
     *
     * Substitutions is a collection of selectors as keys and components as values.
     * Each tag in dom-model is checked for matching
     * a selector, if tag matches when selectors value (component) will be used as tag's component,
     * rather then creating new one.
     *
     * @param component object, to whom dom-model will be binded
     * @param useControllerAsRoot if true, use component as root component in dom-model
     * @param model dom-model to use
     * @param substitutions substitutions map
     * @return dom-model
     * @throws JDOMException
     * @throws IOException
     */
    public static DomModel bind(Object component,
                                boolean useControllerAsRoot,
                                DomModel model, Map<SelectorGroup,
                                JComponent> substitutions)
            throws JDOMException, IOException {

        if (useControllerAsRoot && component instanceof JComponent) {
            SelectorGroup selector = new SelectorGroup("body");
            substitutions.put(selector, (JComponent) component);
        }
        //convert dom-model to swing components
        DomConverter.toSwing(model, substitutions);
        //tie model
        bind(model, component);
        return model;
    }


    /**
     * Ties dom-model to objects. Any fiels, marked with @Bind will be assigned component, whose tag has
     * id equals to @Bind's value.
     *
     * @param model dom-model
     * @param component object, to whom model is tied
     */
    public static void bind(DomModel model, Object component) {
        //получаем поля класса
        Field[] fields = component.getClass().getDeclaredFields();
        for (Field field : fields) {
            //if field is annotated with @Bind
            Annotation a = field.getAnnotation(Bind.class);
            if (a!=null) {
                //get annotation value
                String id = ((Bind)a).value();
                //get tag with such id
                Tag tag = model.getTagById(id);
                if (tag == null) {
                    logger.warn("Can't bind "+id+": no component with such id found");
                }
                else {
                    //assign component
                    field.setAccessible(true);
                    try {
                        field.set(component, tag.getComponent());
                        logger.debug("Binded "+id);
                    }
                    catch (IllegalAccessException e) {
                        logger.error("Can't set value for "+id+": "+ e.getMessage(), e);
                    }
                    catch (IllegalArgumentException e) {//if component type is not the same as field type
                        logger.error("Can't set value for "+id+". Component type: "+component.getClass()+", field type: "+field.getType(), e);
                    }
                }
            }
        }
    }

}
