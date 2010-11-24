package ru.swing.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.css.CssBlock;
import ru.swing.html.css.StyleParser;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import javax.swing.text.JTextComponent;
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

        Tag html = model.getRootTag();
        Tag head = html.getChildByName("head");
        parseHead(model, head);


        Tag body = html.getChildByName("body");
        JComponent b = convertComponent(body);
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
        }
    }

    /**
     * Выполняет процедуру преобразования тега dom-модели в swing-компонент.
     * @param componentTag тег dom-модели
     * @return swing-компонент
     */
    public static JComponent convertComponent(Tag componentTag) {

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
        JComponent component = componentTag.createComponent();
        componentTag.setComponent(component);
        //вызываем процедуру применения атрибутов тега к компоненту
        componentTag.applyAttributes(component);
        //инициализируем менеджер компоновки в компоненте
        componentTag.handleLayout();
        //обрабатываем дочерние теги
        componentTag.handleChildren();

        return component;
    }




}
