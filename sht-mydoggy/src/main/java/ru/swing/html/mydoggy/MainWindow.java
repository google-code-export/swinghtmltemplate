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
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 26.04.11
 * Time: 11:30
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
    public void applyAttributes(JComponent component) {
        MyDoggyToolWindowManager myDoggyToolWindowManager = (MyDoggyToolWindowManager) component;

        super.applyAttributes(component);
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

    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {

        MyDoggyToolWindowManager myDoggyToolWindowManager = (MyDoggyToolWindowManager) getComponent();

        for (Tag child : getChildren()) {

            if (child instanceof ToolWindow) {

                ToolWindow toolWindowTag = (ToolWindow) child;

                JComponent childComponent = DomConverter.convertComponent(toolWindowTag.getChildren().get(0), substitutions);

                ToolWindowAnchor anchor = ToolWindowAnchor.LEFT;
                if ("left".equals(toolWindowTag.getAlign())) {
                    anchor = ToolWindowAnchor.LEFT;
                }
                else if ("right".equals(toolWindowTag.getAlign())) {
                    anchor = ToolWindowAnchor.RIGHT;
                }
                else if ("top".equals(toolWindowTag.getAlign())) {
                    anchor = ToolWindowAnchor.TOP;
                }
                else if ("bottom".equals(toolWindowTag.getAlign())) {
                    anchor = ToolWindowAnchor.BOTTOM;
                }

                Icon icon = null;
                if (StringUtils.isNotEmpty(toolWindowTag.getIcon())) {
                    icon = new ImageIcon(getClass().getResource(toolWindowTag.getIcon()));
                }
                org.noos.xing.mydoggy.ToolWindow w = myDoggyToolWindowManager.registerToolWindow(
                        toolWindowTag.getId(),
                        toolWindowTag.getTitle(),
                        icon,
                        childComponent,
                        anchor);
                w.setAvailable(true);

                if (StringUtils.isNotEmpty(toolWindowTag.getVisible())) {
                    w.setVisible((Boolean) Utils.convertStringToObject(toolWindowTag.getVisible(), Boolean.class));
                }
            }

            else if (child instanceof ContentWindow) {

                ContentWindow contentWindow = (ContentWindow) child;
                ContentManager contentManager = myDoggyToolWindowManager.getContentManager();

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
