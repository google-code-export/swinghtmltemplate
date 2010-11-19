package ru.swing.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 17:50:20
 * </pre>
 */
public class Binder {

    private static Log logger = LogFactory.getLog(Binder.class);

    public static DomModel bind(Object component) throws JDOMException, IOException {
        final Class<? extends Object> cl = component.getClass();
        String className = cl.getName();
        String path = "/"+className.replaceAll("\\.", "/");
        DomModel model = DomLoader.loadModel(component.getClass().getResourceAsStream(path+".html"));
        DomConverter.toSwing(model);
        bind(model, component);
        return model;        
    }


    public static void bind(DomModel model, Object component) {
        Field[] fields = component.getClass().getDeclaredFields();
        for (Field field : fields) {
            Annotation a = field.getAnnotation(Bind.class);
            if (a!=null) {
                String id = ((Bind)a).value();
                Tag tag = model.getTagById(id);
                if (tag == null) {
                    logger.warn("Can't bind "+id+": no component with such id found");
                }
                else {
                    field.setAccessible(true);
                    try {
                        field.set(component, tag.getComponent());
                        logger.debug("Binded "+id);
                    } catch (IllegalAccessException e) {
                        logger.error("Can't set value for "+id+": "+ e.getMessage(), e);
                    }
                }
            }
        }
    }

}
