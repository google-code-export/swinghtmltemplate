package ru.swing.html.mydoggy;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.noos.xing.mydoggy.ContentManager;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import org.noos.xing.mydoggy.plaf.ui.content.MyDoggyDesktopContentManagerUI;
import org.noos.xing.mydoggy.plaf.ui.content.MyDoggyMultiSplitContentManagerUI;
import org.noos.xing.mydoggy.plaf.ui.content.MyDoggyTabbedContentManagerUI;
import ru.swing.html.DomConverter;
import ru.swing.html.Utils;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.util.Map;

/**
 * The main window for mydoggy content.
 *
 * Content manager ui can be set with "type" attribute:
 * <ul>
 *     <li>split - MyDoggyMultiSplitContentManagerUI</li>
 *     <li>tabbed - MyDoggyTabbedContentManagerUI</li>
 *     <li>desktop - MyDoggyDesktopContentManagerUI</li>
 * </ul>
 *
 * The only allowed children are:
 * <ul>
 *    <li>&lt;mydoggy:toolWindow&gt; - for tool window</li>
 *    <li>&lt;mydoggy:contentWindow&gt; - for content window</li>
 * </ul>
 */
public class MainWindow extends Tag {

    private Log logger = LogFactory.getLog(getClass());
    
    @Override
    public JComponent createComponent() {
        MyDoggyToolWindowManager myDoggyToolWindowManager = new MyDoggyToolWindowManager();
        setComponent(myDoggyToolWindowManager);
        return myDoggyToolWindowManager;
    }


    @Override
    public void handleLayout() {
    }


    @Override
    public void applyAttribute(JComponent component, String attrName) {
        MyDoggyToolWindowManager myDoggyToolWindowManager = (MyDoggyToolWindowManager) component;


        if (TYPE_ATTRIBUTE.equals(attrName)) {
            //set content manager ui
            if (StringUtils.isNotEmpty(getType())) {

                if ("split".equals(getType())) {
                    myDoggyToolWindowManager.getContentManager().setContentManagerUI(new MyDoggyMultiSplitContentManagerUI());
                }
                else if ("tabbed".equals(getType())) {
                    myDoggyToolWindowManager.getContentManager().setContentManagerUI(new MyDoggyTabbedContentManagerUI());
                }
                else if ("desktop".equals(getType())) {
                    myDoggyToolWindowManager.getContentManager().setContentManagerUI(new MyDoggyDesktopContentManagerUI());
                }
                else {
                    logger.warn(toString()+ ": unknown type '"+getType()+"'");
                }

            }
        }
        else {
            super.applyAttribute(component, attrName);
        }
    }

    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {

        MyDoggyToolWindowManager myDoggyToolWindowManager = (MyDoggyToolWindowManager) getComponent();

        //add tool windows and content windows
        for (Tag child : getChildren()) {

            if (child instanceof ToolWindow) {
                //add tool window
                ToolWindow toolWindow = (ToolWindow) child;

                if (toolWindow.getChildren()==null || toolWindow.getChildren().isEmpty()) {
                    logger.warn(toolWindow+" doesn't contain any children. It must contain 1 child. Skipping tool window");
                }
                else {

                    if (toolWindow.getChildren().size()>1) {
                        logger.warn(toolWindow+" contains more than 1 child. Using the first one.");
                    }
                    JComponent childComponent = DomConverter.convertComponent(toolWindow.getChildren().get(0), substitutions);

                    ToolWindowAnchor anchor = ToolWindowAnchor.LEFT;
                    if ("left".equals(toolWindow.getAlign())) {
                        anchor = ToolWindowAnchor.LEFT;
                    }
                    else if ("right".equals(toolWindow.getAlign())) {
                        anchor = ToolWindowAnchor.RIGHT;
                    }
                    else if ("top".equals(toolWindow.getAlign())) {
                        anchor = ToolWindowAnchor.TOP;
                    }
                    else if ("bottom".equals(toolWindow.getAlign())) {
                        anchor = ToolWindowAnchor.BOTTOM;
                    }

                    Icon icon = null;
                    if (StringUtils.isNotEmpty(toolWindow.getIcon())) {
                        icon = new ImageIcon(getClass().getResource(toolWindow.getIcon()));
                    }
                    org.noos.xing.mydoggy.ToolWindow w = myDoggyToolWindowManager.registerToolWindow(
                            toolWindow.getId(),
                            toolWindow.getTitle(),
                            icon,
                            childComponent,
                            anchor);
                    w.setAvailable(true);

                    if (StringUtils.isNotEmpty(toolWindow.getVisible())) {
                        w.setVisible((Boolean) Utils.convertStringToObject(toolWindow.getVisible(), Boolean.class));
                    }

                }
            }
            //add content window
            else if (child instanceof ContentWindow) {

                ContentWindow contentWindow = (ContentWindow) child;
                ContentManager contentManager = myDoggyToolWindowManager.getContentManager();

                if (contentWindow.getChildren()==null || contentWindow.getChildren().isEmpty()) {
                    logger.warn(contentWindow+" doesn't contain any children. It must contain 1 child. Skipping content window");
                }
                else {
                    if (contentWindow.getChildren().size()>1) {
                        logger.warn(contentWindow+" contains more than 1 child. Using the first one.");
                    }
                    JComponent childComponent = DomConverter.convertComponent(contentWindow.getChildren().get(0), substitutions);

                    Icon icon = null;
                    if (StringUtils.isNotEmpty(contentWindow.getIcon())) {
                        icon = new ImageIcon(getClass().getResource(contentWindow.getIcon()));
                    }
                    contentManager.addContent(contentWindow.getId(), contentWindow.getTitle(), icon, childComponent);
                }

            }
            
        }


    }
}
