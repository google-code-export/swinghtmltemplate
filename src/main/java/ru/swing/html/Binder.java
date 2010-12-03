package ru.swing.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Вспомогательный класс для привязки dom-модели к классу с полями, помеченными аннотацией @Bind
 */
public class Binder {

    private static Log logger = LogFactory.getLog(Binder.class);

    /**
     * Создает модель для объекта и выполняет привязку к модели.
     * dom-модель загружается из файла, совпадающего с именем класса, расширением html,
     * и лежащего в том же месте, где и класс.
     *
     * Например, для класса foo.Foo будет загружен файл /foo/Foo.html
     *
     * @param component объект, к которому выполняется привязка dom-модели
     * @return dom-модель html-документа
     * @throws JDOMException
     * @throws IOException
     */
    public static DomModel bind(Object component) throws JDOMException, IOException {
        //получаем имя класса
        final Class<? extends Object> cl = component.getClass();
        String className = cl.getName();
        //получаем путь к файлу, меняя точки на '/'
        String path = "/"+className.replaceAll("\\.", "/") + ".html";
        //загружаем модель
        InputStream htmlStream = component.getClass().getResourceAsStream(path);
        if (htmlStream!=null) {
            DomModel model = DomLoader.loadModel(htmlStream);
            //преобразуем модель в компоненты
            DomConverter.toSwing(model);
            //привязываем модель
            bind(model, component);
            return model;
        }
        else {
            logger.error("Can't find html-document at "+path);
            return null;
        }
    }


    /**
     * Привязывает dom-модель к объекту. Всем полям, помеченным аннотацией @Bind, будет присвоен компонент, тег
     * которого имеет идентификатор, указаный в качестве значения аннотации.
     * @param model dom-модель
     * @param component объект, к которому привязывается dom-модель
     */
    public static void bind(DomModel model, Object component) {
        //получаем поля класса
        Field[] fields = component.getClass().getDeclaredFields();
        for (Field field : fields) {
            //если поле помечено аннотацией @Bind
            Annotation a = field.getAnnotation(Bind.class);
            if (a!=null) {
                //получаем значение идентификатора из аннотации
                String id = ((Bind)a).value();
                //получаем тег с указанным идентификатором
                Tag tag = model.getTagById(id);
                if (tag == null) {
                    logger.warn("Can't bind "+id+": no component with such id found");
                }
                else {
                    //присваиваем компонент
                    field.setAccessible(true);
                    try {
                        field.set(component, tag.getComponent());
                        logger.debug("Binded "+id);
                    }
                    catch (IllegalAccessException e) {
                        logger.error("Can't set value for "+id+": "+ e.getMessage(), e);
                    }
                    catch (IllegalArgumentException e) {//если тип компонента не совпадает с типом поля
                        logger.error("Can't set value for "+id+". Component type: "+component.getClass()+", field type: "+field.getType(), e);
                    }
                }
            }
        }
    }

}
