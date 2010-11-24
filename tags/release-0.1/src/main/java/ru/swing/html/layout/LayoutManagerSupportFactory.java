package ru.swing.html.layout;

import ru.swing.html.tags.Tag;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 11:52:31
 * </pre>
 */
public class LayoutManagerSupportFactory {
    public static LayoutManagerSupport createLayout(Tag tag) {
        String layoutName = tag.getDisplay();
        LayoutManagerSupport res;

        if ("p".equals(tag.getName())) {
            res = new FlowLayoutSupport();
        }
        else if ("border".equals(layoutName)) {
            res = new BorderLayoutSupport();
        }
        else if ("flow".equals(layoutName)) {
            res = new FlowLayoutSupport();
        }
        else if ("table".equals(layoutName)) {
            res = new TableLayoutSupport();
        }
        else if ("mig".equals(layoutName)) {
            res = new MigLayoutSupport();
        }
        else if ("box".equals(layoutName)) {
            res = new BoxLayoutSupport();
        }
        else {
            res = new FlowLayoutSupport();
        }

        return res;

    }
}
