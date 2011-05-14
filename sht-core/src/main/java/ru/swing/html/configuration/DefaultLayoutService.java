package ru.swing.html.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.layout.*;
import ru.swing.html.tags.Tag;

import java.util.HashMap;
import java.util.Map;

/**
 * This service holds the map <layout_id, LayoutManagerSupport> for default supported layouts.
 * It also supports adding new ids.
 */
public class DefaultLayoutService implements LayoutService {

    private Map<String, Class<? extends LayoutManagerSupport>> layouts = new HashMap<String, Class<? extends LayoutManagerSupport>>();
    private Class<? extends LayoutManagerSupport> defaultLayout;
    private Log logger = LogFactory.getLog(getClass());

    public DefaultLayoutService() {
        addLayoutManagerSupport("absolute", AbsoluteLayoutSupport.class);
        addLayoutManagerSupport("border", BorderLayoutSupport.class);
        addLayoutManagerSupport("card", CardLayoutSupport.class);
        addLayoutManagerSupport("flow", FlowLayoutSupport.class);
        addLayoutManagerSupport("table", TableLayoutSupport.class);
        addLayoutManagerSupport("mig", MigLayoutSupport.class);
        addLayoutManagerSupport("box", BoxLayoutSupport.class);

        defaultLayout = FlowLayoutSupport.class;
    }


    public void addLayoutManagerSupport(String layoutId, Class<? extends LayoutManagerSupport> layoutManagerSupportClass) {
        if (layouts.containsKey(layoutId)) {
            logger.warn("'"+layoutId+"' is already added with class: "+layouts.get(layoutId).getName()+", replacing existing");
        }
        layouts.put(layoutId, layoutManagerSupportClass);
        logger.trace("Added layout manager support for '"+layoutId+"': "+layoutManagerSupportClass.getName());
    }

    public LayoutManagerSupport createLayout(Tag tag) {
        String layoutName = tag.getDisplay();
        Class<? extends LayoutManagerSupport> res;

        if (layouts.containsKey(layoutName)) {
            res = layouts.get(layoutName);
        }
        else if (layoutName == null) {
            res = defaultLayout;
        }
        else {
            logger.warn(tag+": '"+ layoutName+"' layout is not supported, using default: "+defaultLayout.getName());
            res = defaultLayout;
        }

        LayoutManagerSupport layoutManagerSupport = null;
        try {
            layoutManagerSupport = res.newInstance();
        } catch (InstantiationException e) {
            logger.warn("Can't create layout '"+layoutName+"': "+e.getMessage());
        } catch (IllegalAccessException e) {
            logger.warn("Can't create layout '"+layoutName+"': "+e.getMessage());
        }
        return layoutManagerSupport;
    }
}
