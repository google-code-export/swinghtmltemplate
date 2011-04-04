package ru.swing.html;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.css.CssBlock;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.css.StyleParser;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 11:03:51
 * </pre>
 */
public class DomConverter {

    private static Log logger = LogFactory.getLog(DomConverter.class);

    /**
     * Для каждого тега dom-модели производит построение соответствующего swing-компонета.
     * @param model dom-модель
     * @return корневой swing-компонент
     */
    public static JComponent toSwing(DomModel model) {
        return toSwing(model, Collections.<SelectorGroup, JComponent>emptyMap());
    }

    /**
     * Для каждого тега dom-модели производит построение соответствующего swing-компонета.
     * @param model dom-модель
     * @param substitutions карта подстановок компонентов. Ключ - селектор, значение - компонент.
     * @return корневой swing-компонент
     */
    public static JComponent toSwing(DomModel model, Map<SelectorGroup, JComponent> substitutions) {

        Tag html = model.getRootTag();
        Tag head = html.getChildByName("head");
        parseHead(model, head);


        Tag body = html.getChildByName("body");
        JComponent b = convertComponent(body, substitutions);
        for (Tag tag : model.query("*")) {
            tag.afterComponentsConverted();
        }
        return b;
    }

    /**
     * Обрабатывает тег &lt;head&gt;
     * @param model dom-модель
     * @param head тег
     */
    public static void parseHead(DomModel model, Tag head) {
        if (head==null) {
            return;
        }
        for (Tag headChild : head.getChildren()) {
            if ("style".equals(headChild.getName())) {
                String css = headChild.getContent();
                List<String> cssBlocks = StyleParser.extractCssBlocks(css);
                for (String cssBlock : cssBlocks) {
                    CssBlock block = StyleParser.parseCssBlock(cssBlock);
                    model.addGlobalStyle(block);
                }
            }
            else if ("link".equals(headChild.getName()) && "stylesheet".equals(headChild.getAttribute("rel"))) {
                String filename = headChild.getAttribute("href");
                if (StringUtils.isEmpty(filename)) {
                    return;
                }

                //if absolute path
                if (filename.startsWith("/")) {
                    //getClassLoader().getResourceAsStream понимает пути без ведущего /, например,
                    //"ru/swing/html/example/loginform.css"
                    filename = filename.substring(1);
                }
                //if relative path - substitute path from dom model path
                else if (StringUtils.isNotEmpty(model.getSourcePath())) {
                    String parentPath = model.getSourcePath();
                    int indexOfFilename = parentPath.lastIndexOf('/');
                    if (indexOfFilename>=0) {
                        parentPath = parentPath.substring(0, indexOfFilename);
                    }
                    if (parentPath.startsWith("/")) {
                        parentPath = parentPath.substring(1);
                    }

                    filename = parentPath + "/" + filename;
                }

                InputStream in = DomConverter.class.getClassLoader().getResourceAsStream(filename);
                if (in!=null) {
                    try {
                        String css = Utils.readStringIntoString(in);
                        List<String> cssBlocks = StyleParser.extractCssBlocks(css);
                        for (String cssBlock : cssBlocks) {
                            CssBlock block = StyleParser.parseCssBlock(cssBlock);
                            model.addGlobalStyle(block);
                        }
                    } catch (IOException e) {
                        logger.warn("Can't read css file: "+filename);
                    }
                }
                else {
                    logger.warn("css file: "+filename+" not found");
                }
            }
        }
    }

    /**
     * Выполняет процедуру преобразования тега dom-модели в swing-компонент.
     * @param componentTag тег dom-модели
     * @return swing-компонент
     */
    public static JComponent convertComponent(Tag componentTag) {
        return convertComponent(componentTag, Collections.<SelectorGroup, JComponent>emptyMap());
    }

    /**
     * Выполняет процедуру преобразования тега dom-модели в swing-компонент.
     * @param componentTag тег dom-модели
     * @param substitutions карта подстановок компонентов. Ключ - селектор, значение - компонент.
     * @return swing-компонент
     */
    public static JComponent convertComponent(Tag componentTag, Map<SelectorGroup, JComponent> substitutions) {

        //если dom-модель тега не null, то попробуем применить на тег таблицу css стилей.
        if (componentTag.getModel()!=null) {
            //сохраняем имеющиеся атрибуты тега (они получены на этапе загрузки dom-модели)
            Map<String, String> old = new HashMap<String, String>(componentTag.getAttributes());

            //применяем к тегу глобальные css-стили документа
            List<CssBlock> css = componentTag.getModel().getGlobalStyles();
            for (CssBlock block : css) {
                if (block.matches(componentTag)) {
                    for (String attrName : block.getStyles().keySet()) {
                        componentTag.setAttribute(attrName, block.getStyles().get(attrName));
                    }
                }
            }
            //теперь применяем атрибуты тега, таким образом локальные атрибуты перекрывают глобальные
            for (String attrName : old.keySet()) {
                componentTag.setAttribute(attrName, old.get(attrName));
            }
        }

        //вызываем создание тегом компонента
        JComponent component = null;
        //если компонент есть в карте подстановок, то в качестве компонента используем компонент из карты
        for (SelectorGroup selector : substitutions.keySet()) {
            if (selector.matches(componentTag)) {
                component = substitutions.get(selector);
                break;
            }
        }
        if (component==null) {
            component = componentTag.createComponent();
        }
        componentTag.setComponent(component);
        //вызываем процедуру применения атрибутов тега к компоненту
        componentTag.applyAttributes(component);
        //инициализируем менеджер компоновки в компоненте
        componentTag.handleLayout();
        //обрабатываем дочерние теги
        componentTag.handleChildren(substitutions);

        return component;
    }




}
