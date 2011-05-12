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
import ru.swing.html.layout.LayoutManagerSupport;
import ru.swing.html.tags.Tag;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * This layout manager support allows to use mydoggy without &lt;mydoggy:mainWindow> and simular tags.
 * Just set the "display" attribute to "mydoggy" and the component will be converted to MyDoggy container.
 */
public class MydoggyLayoutManagerSupport implements LayoutManagerSupport {


    private Log logger = LogFactory.getLog(getClass());

    public void addComponent(JComponent parent, JComponent child, Tag childTag, String constraint) {
        MyDoggyToolWindowManager windowManager = (MyDoggyToolWindowManager) parent;
        if (StringUtils.isNotEmpty(constraint)) {
            ToolWindowAnchor anchor = ToolWindowAnchor.LEFT;
            if ("left".equals(constraint)) {
                anchor = ToolWindowAnchor.LEFT;
            }
            else if ("right".equals(constraint)) {
                anchor = ToolWindowAnchor.RIGHT;
            }
            else if ("top".equals(constraint)) {
                anchor = ToolWindowAnchor.TOP;
            }
            else if ("bottom".equals(constraint)) {
                anchor = ToolWindowAnchor.BOTTOM;
            }

            Icon icon = null;
            String iconStr = childTag.getAttribute("icon");
            if (StringUtils.isNotEmpty(iconStr)) {

                try {
                    InputStream inputStream = childTag.getModel().getConfiguration().getResourceLoader().loadResource(childTag.getModel(), iconStr);
                    Image img = ImageIO.read(inputStream);
                    icon = new ImageIcon(img);
                } catch (IOException e) {
                    logger.warn(childTag+": can't load resource '"+iconStr+"': "+e.getMessage());
                }
            }
            org.noos.xing.mydoggy.ToolWindow w = windowManager.registerToolWindow(
                    childTag.getId(),
                    childTag.getAttribute("title"),
                    icon,
                    child,
                    anchor);
            w.setAvailable(true);

            String visible = childTag.getAttribute("visible");
            if (StringUtils.isNotEmpty(visible)) {
                w.setVisible(Utils.convertStringToObject(visible, Boolean.class));
            }

        }
        else {
            ContentManager contentManager = windowManager.getContentManager();


            String iconStr = childTag.getAttribute("icon");
            Icon icon = null;
            if (StringUtils.isNotEmpty(iconStr)) {

                try {
                    InputStream inputStream = childTag.getModel().getConfiguration().getResourceLoader().loadResource(childTag.getModel(), iconStr);
                    Image img = ImageIO.read(inputStream);
                    icon = new ImageIcon(img);
                } catch (IOException e) {
                    logger.warn(childTag+": can't load resource '"+iconStr+"': "+e.getMessage());
                }
            }
            contentManager.addContent(childTag.getId(), childTag.getAttribute("title"), icon, child);

        }
    }

    public LayoutManager createLayout(Tag tag) {
        MyDoggyToolWindowManager windowManager = new MyDoggyToolWindowManager();
        tag.setComponent(windowManager);

        //set content manager ui
        if (StringUtils.isNotEmpty(tag.getType())) {

            if ("split".equals(tag.getType())) {
                windowManager.getContentManager().setContentManagerUI(new MyDoggyMultiSplitContentManagerUI());
            }
            else if ("tabbed".equals(tag.getType())) {
                windowManager.getContentManager().setContentManagerUI(new MyDoggyTabbedContentManagerUI());
            }
            else if ("desktop".equals(tag.getType())) {
                windowManager.getContentManager().setContentManagerUI(new MyDoggyDesktopContentManagerUI());
            }
            else {
                logger.warn(toString()+ ": unknown type '"+tag.getType()+"'");
            }

        }


        return windowManager.getLayout();
    }
}
