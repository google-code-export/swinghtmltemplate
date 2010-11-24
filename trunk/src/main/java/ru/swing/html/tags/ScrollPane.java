package ru.swing.html.tags;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.DomConverter;

import javax.swing.*;

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
    public void handleChildren() {
        if (getComponent() instanceof JScrollPane) {
            JScrollPane pane = (JScrollPane) getComponent();
            if (getChildren().size()>=1) {
                JComponent childComponent = DomConverter.convertComponent(getChildren().get(0));
                pane.setViewportView(childComponent);
            }

            if (getChildren().size()>1) {
                logger.warn("scroll can't contain more then 1 child");
            }
        }

    }
}
