package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import ru.swing.html.ELUtils;

import javax.swing.*;

/**
 * <p>
 *     Tag is converted to `javax.swing.JLabel. The content of the tag is assigned as a component's text.
 * </p>
 * <p>
 *     If "content" equals to "html", then the content of the tag is surrounded with &lt;html> before and &lt;/html> after,
 *     so "JLabel" will produce html. If `content` equals to "text" or is empty,
 *     then the tag content is just assigned as a component's text.
 * </p>
 * <h2>Example:</h2>
 * <pre>
 * &lt;p>Login:&lt;/p>
 * &lt;p content="html">&lt;![CDATA[&lt;u>Login:&lt;/u>]]>&lt;/p>
 * &lt;p content="text">&lt;![CDATA[&lt;html>&lt;u>Login:&lt;/u>&lt;/html>]]>&lt;/p>
 * </pre>
 */
public class P extends Tag {


    private Log logger = LogFactory.getLog(getClass());
    public static final String CONTENT_ATTRIBUTE = "content";
    private String contentType;

    @Override
    public JComponent createComponent() {
        JLabel label = new JLabel();
        setComponent(label);
        return label;
    }

    @Override
    public void applyAttribute(JComponent component, String attrName) {

        JLabel label = (JLabel) component;
        if (CONTENT_ATTRIBUTE.equals(attrName) || TAG_CONTENT.equals(attrName)) {

            String contentType = getContentType();

            if ("html".equals(contentType)) {
                String text = StringUtils.isNotEmpty(getContent()) ? ELUtils.parseStringValue(getContent(), getModelElements()) : getContent();
                label.setText("<html>"+text+"</html>");
            }
            else if ("text".equals(contentType)) {
                String text = StringUtils.isNotEmpty(getContent()) ? ELUtils.parseStringValue(getContent(), getModelElements()) : getContent();
                label.setText(text);
            }
            else if ("el".equals(contentType)) {
                bind(getContent(), label, BeanProperty.create("text"), AutoBinding.UpdateStrategy.READ_WRITE);
            }
            else if (StringUtils.isEmpty(contentType)) {
                String text = StringUtils.isNotEmpty(getContent()) ? ELUtils.parseStringValue(getContent(), getModelElements()) : getContent();
                label.setText(text);
            }
            else {
                logger.warn("Unknown type: "+ contentType +", defaulting to text");
                label.setText(ELUtils.parseStringValue(getContent(), createContextModel()));
            }
        }
        else {
            super.applyAttribute(component, attrName);
        }
    }

    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);

        if (CONTENT_ATTRIBUTE.equals(name)) {
            setContentType(value);
        }

    }

    @Override
    public void handleLayout() {
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
