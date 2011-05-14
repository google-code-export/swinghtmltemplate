package ru.swing.html.layout;

import ru.swing.html.tags.Tag;

/**
 * <p>
 * Factory to produce LayoutManagerSupport, based on 'display' tag attribute value.
 * </p>
 * <p>
 *     Possible values:
 *     <ul>
 *         <li>absolute - for AbsoluteLayoutSupport</li>
 *         <li>border - for BorderLayoutSupport</li>
 *         <li>box - for BoxLayoutSupport</li>
 *         <li>flow - for FlowLayoutSupport</li>
 *         <li>mig - for MigLayoutSupport</li>
 *         <li>table - for TableLayoutSupport</li>
 *     </ul>
 * </p>
 * <p>
 * If no value is set to 'display' attribute, or unknown value is set, FlowLayoutSupport is used.
 * </p>
 * @see LayoutManagerSupport
 */
@Deprecated
public class LayoutManagerSupportFactory {
    public static LayoutManagerSupport createLayout(Tag tag) {
        String layoutName = tag.getDisplay();
        LayoutManagerSupport res;

        if ("absolute".equals(layoutName)) {
            res = new AbsoluteLayoutSupport();
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
