package ru.swing.html.tags.ui;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.*;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.util.*;

/**
 * <p>
 * This tag loads messages from resource bundle and stores them as Map in dom model under the specified name.
 * </p>
 * <p>
 *     The locale can be specified via
 *     <ul>
 *         <li>"locale" attribute</li>
 *         <li>"locale" meta tag in the head section of the document</li>
 *     </ul>
 *     If no one is specified, the default is used (Locale.getDefault())
 * </p>
 *
 */
public class LoadBundle extends Tag {

    private Log logger = LogFactory.getLog(getClass());
    private String var;
    private String baseName;
    private String locale;

    @Override
    public JComponent createComponent() {
        return null;
    }

    @Override
    public void handleLayout() {
    }

    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {
    }

    @Override
    public void setAttribute(String name, String value) {
        if ("var".equals(name)) {
            setVar(value);
        }
        else if ("basename".equals(name)) {
            setBaseName(value);
        }
        else if ("locale".equals(name)) {
            setLocale(value);
        }
        super.setAttribute(name, value);
    }

    @Override
    public void beforeComponentsConvertion() {

        Locale locale;
        if (StringUtils.isNotEmpty(getLocale())) {
            String localeStr = getLocale();
            locale = parseLocaleString(localeStr);
        }
        else if (getModel().getMetaItems().containsKey("locale")) {
            String localeStr = getModel().getMetaItems().get("locale");
            locale = parseLocaleString(localeStr);
        }
        else {
            locale = Locale.getDefault();
        }
        logger.trace(toString()+": Loading resource bundle from "+getBaseName()+", '"+locale+"' locale is used.");


        ResourceBundle rb = ResourceBundle.getBundle(getBaseName(), locale);
        getModel().addModelElement(getVar(), convertResourceBundleToMap(rb));
    }

    private Locale parseLocaleString(String localeString) {
        Locale locale;
        String[] tokens = localeString.split("_");
        if (tokens.length>1) {
            locale = new Locale(tokens[0], tokens[1]);
        }
        else {
            locale = new Locale(tokens[0]);
        }
        return locale;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * <a href='http://www.kodejava.org/examples/340.html'>http://www.kodejava.org/examples/340.html</a>
     * <p>Convert ResourceBundle into a Map object.</p>
     *
     * @param resource a resource bundle to convert.
     * @return Map a map version of the resource bundle.
     */
    private static Map<String, String> convertResourceBundleToMap(ResourceBundle resource) {
        Map<String, String> map = new HashMap<String, String>();

        Enumeration<String> keys = resource.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            map.put(key, resource.getString(key));
        }

        return map;
    }

}
