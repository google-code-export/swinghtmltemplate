package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import ru.swing.html.ELUtils;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 20.11.2010
 * Time: 23:37:43
 */
public class P extends Tag {


    private Log logger = LogFactory.getLog(getClass());
    public static final String CONTENT_ATTRIBUTE = "content";

    @Override
    public JComponent createComponent() {
        JLabel label = new JLabel();
        setComponent(label);
        return label;
    }

    @Override
    public void applyAttributes(JComponent component) {
        super.applyAttributes(component);
        JLabel label = (JLabel) component;
        String contentType = getAttribute(CONTENT_ATTRIBUTE);
        if ("html".equals(contentType)) {
            String text = StringUtils.isNotEmpty(getContent()) ? ELUtils.parseStringValue(getContent(), createContextModel()) : getContent();
            label.setText("<html>"+text+"</html>");
        }
        else if ("text".equals(contentType)) {
            String text = StringUtils.isNotEmpty(getContent()) ? ELUtils.parseStringValue(getContent(), createContextModel()) : getContent();
            label.setText(text);
        }
        else if ("el".equals(contentType)) {
            bind(getContent(), label, BeanProperty.create("text"), AutoBinding.UpdateStrategy.READ_WRITE);
        }
        else if (StringUtils.isEmpty(contentType)) {
            String text = StringUtils.isNotEmpty(getContent()) ? ELUtils.parseStringValue(getContent(), createContextModel()) : getContent();
            label.setText(text);
        }
        else {
            logger.warn("Unknown type: "+ contentType +", defaulting to text");
            label.setText(ELUtils.parseStringValue(getContent(), createContextModel()));
        }
    }

    @Override
    public void handleLayout() {
    }



}
