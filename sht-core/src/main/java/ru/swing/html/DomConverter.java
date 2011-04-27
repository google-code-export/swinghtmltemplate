package ru.swing.html;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.css.CssBlock;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.css.StyleParser;
import ru.swing.html.tags.Meta;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.awt.*;
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
     * <p>Creates an window. The window type is set with</p>
     * <pre>
     *     &lt;meta name='display-as' content='frame'/&gt;
     * </pre>
     * <p>Possible values for "display-as" are "frame" and "dialog".</p>
     *
     * <p>If originalWindow is not null, when window is not created, originalWindow will be
     * used instead.</p>
     *
     * <p>The window title, defaultCloseOperation, modality (for JDialog) are set for window.
     * The title is taken from &lt;title>Title&lt;/title> tag.</p>
     *
     * <p>defaultCloseOperation is set with
     * <pre>
     *     &lt;meta name='onclose' content='close_op'/&gt;
     * </pre>
     * Possible values for "close_op" are:
     * <ul>
     *     <li>exit - JFrame.EXIT_ON_CLOSE (only for JFrame)</li>
     *     <li>hide - JFrame.HIDE_ON_CLOSE</li>
     *     <li>dispose - JFrame.DISPOSE_ON_CLOSE</li>
     *     <li>nothing - JFrame.DO_NOTHING_ON_CLOSE</li>
     * </ul>
     * </p>
     *
     * <p>The modality of JDialog can be set with
     * <pre>
     *     &lt;meta name='modal' content='true|false'/&gt;
     * </pre>
     * </p>
     *
     * @param model dom-model
     * @param originalWindow if not null, will be used as window.
     * @return created window
     */
    public static Window createWindow(DomModel model, Window originalWindow) {
        Window res;
        if (originalWindow==null) {
            if ("dialog".equals(model.getMetaItems().get("display-as"))) {
                res = new JDialog();

            }
            else {
                res = new JFrame();
            }
        }
        else {
            res = originalWindow;
        }


        //apply metas
        if (res instanceof JDialog) {
            JDialog dlg = (JDialog) res;
            Tag[] titles = model.query("title");
            if (titles.length>0) {
                dlg.setTitle(titles[0].getContent());
            }

            String onclose = model.getMetaItems().get("onclose");
            if (StringUtils.isNotEmpty(onclose)) {
                if ("hide".equals(onclose)) {
                    dlg.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
                }
                else if ("dispose".equals(onclose)) {
                    dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                }
                else if ("nothing".equals(onclose)) {
                    dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                }
            }

            dlg.setModal((Boolean) Utils.convertStringToObject(model.getMetaItems().get("modal"), Boolean.class));


        }
        else if (res instanceof JFrame) {
            JFrame frame = (JFrame) res;
            Tag[] titles = model.query("title");
            if (titles.length>0) {
                frame.setTitle(titles[0].getContent());
            }

            String onclose = model.getMetaItems().get("onclose");
            if (StringUtils.isNotEmpty(onclose)) {
                if ("exit".equals(onclose)) {
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
                else if ("hide".equals(onclose)) {
                    frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                }
                else if ("dispose".equals(onclose)) {
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                }
                else if ("nothing".equals(onclose)) {
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }

        }

        return res;
    }


    /**
     * Creates swing component for every tag in the dom model.
     *
     * <p>When &lt;meta name='display-as' content='frame'&gt; is contained inside &lt;head&gt;
     * a window will be created (or existing window will be used if set with setWindow()),
     * the root component will be added into the contentPane of the window.</p>
     *
     * <p>Possible values for 'display-as' are "frame" and "dialog".</p>
     *
     * @param model dom-model
     * @return root swing-component
     */
    public static JComponent toSwing(DomModel model) {
        return toSwing(model, Collections.<SelectorGroup, JComponent>emptyMap());
    }

    /**
     * Creates swing component for every tag in the dom model.
     *
     * <p>When &lt;meta name='display-as' content='frame'&gt; is contained inside &lt;head&gt;
     * a window will be created (or existing window will be used if set with setWindow()),
     * the root component will be added into the contentPane of the window.</p>
     *
     * <p>Possible values for 'display-as' are "frame" and "dialog".</p>
     *
     * @param model dom-model
     * @param substitutions the map of the substitutions of the components. The key - css selector, value - the component to be used.
     * @return root swing-component
     */
    public static JComponent toSwing(DomModel model, Map<SelectorGroup, JComponent> substitutions) {

        logger.trace("Converting dom model to swing components");
        Tag html = model.getRootTag();
        Tag head = html.getChildByName("head");
        parseHead(model, head);


        Tag body = html.getChildByName("body");
        JComponent b = convertComponent(body, substitutions);
        for (Tag tag : model.query("*")) {
            tag.afterComponentsConverted();
        }

        if (model.getMetaItems().containsKey("display-as")) {
            Window w = createWindow(model, model.getWindow());
            model.setWindow(w);
            if (w instanceof JFrame) {
                ((JFrame)w).getContentPane().add(b);
            }
            else if (w instanceof JDialog) {
                ((JDialog)w).getContentPane().add(b);
            }
        }

        return b;
    }

    /**
     * Parses tag &lt;head&gt;
     * @param model dom-model
     * @param head tag
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
            else if ("meta".equals(headChild.getName())) {
                Meta meta = (Meta) headChild;
                model.getMetaItems().put(meta.getMetaName(), meta.getMetaContent());
            }
        }
    }

    /**
     * Converts single tag to the swing component
     * @param componentTag tag dom-model
     * @return swing-component
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
