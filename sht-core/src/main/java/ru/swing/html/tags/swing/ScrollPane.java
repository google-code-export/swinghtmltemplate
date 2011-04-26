package ru.swing.html.tags.swing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.DomConverter;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * User: Penkov Vladimir
 * Date: 19.11.2010
 * Time: 16:04:52
 * </pre>
 */
public class ScrollPane extends Tag {

    private Log logger = LogFactory.getLog(getClass());

    @Override
    public JComponent createComponent() {
        JScrollPane scrollPane = new JScrollPane();
        setComponent(scrollPane);
        return scrollPane;
    }

    @Override
    public void handleLayout() {
    }

    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {
        if (getComponent() instanceof JScrollPane) {
            JScrollPane pane = (JScrollPane) getComponent();

            List<Tag> childrenWithComponents = new ArrayList<Tag>();
            for (Tag child : getChildren()) {
                JComponent childComponent = DomConverter.convertComponent(child, substitutions);
                if (childComponent!=null) {
                    childrenWithComponents.add(child);
                }
            }

            if (childrenWithComponents.size()>=1) {
                JComponent childComponent = childrenWithComponents.get(0).getComponent();
                pane.setViewportView(childComponent);
            }

            if (childrenWithComponents.size()>1) {
                logger.warn("scroll can't contain more then 1 child");
            }
        }

    }
}
