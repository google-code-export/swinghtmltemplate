package ru.swing.html.tags.swing;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.ELUtils;
import ru.swing.html.IOUtils;
import ru.swing.html.configuration.MethodInvoker;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.InputStream;

/**
 *
 * Actions are injected into root component immediatelly after conversion phase and before after-conversion phase.
 */
public class Action extends Tag {

    private String actionName;
    private String onAction;
    private String title;
    private String icon;
    private String shortcut;
    private String scope;
    private Log logger = LogFactory.getLog(getClass());

    @Override
    public JComponent getComponent() {
        return null;
    }

    @Override
    public void handleLayout() {
    }



    @Override
    public void setAttribute(String name, String value) {
        if ("actionname".equals(name)) {
            actionName = value;
        }
        else if ("onaction".equals(name)) {
            onAction = value;
        }
        else if ("title".equals(name)) {
            title = value;
        }
        else if ("icon".equals(name)) {
            icon = value;
        }
        else if ("scope".equals(name)) {
            scope = value;
        }
        else if ("shortcut".equals(name)) {
            shortcut = value;
        }
        super.setAttribute(name, value);
    }

    public String getActionName() {
        return actionName;
    }

    public String getOnAction() {
        return onAction;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }

    public String getShortcut() {
        return shortcut;
    }

    /**
     * Sets the keyboard shortcut for this action. This shortcut will be used as key when
     * inserting this action into inputMap of root component.
     *
     * Must be set in format of KeyStroke#getKeyStroke(String).
     *
     * @param shortcut shortcut for this action
     * @see KeyStroke#getKeyStroke(String)
     */
    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    /**
     * Returns the inputMap scope into which the keyboard shortcut will be installed.
     * @return scope
     */
    public String getScope() {
        return scope;
    }

    /**
     * Returns the inputMap scope into which the keyboard shortcut will be installed.
     * Values: 'ancestor', 'window', 'focused'
     * @param scope
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    public AbstractAction createSwingAction() {

        final MethodInvoker invoker;
        if (StringUtils.isNotEmpty(getOnAction())) {
            invoker = getModel().getConfiguration().getMethodResolverService().resolveMethod(getOnAction(), this);
        }
        else {
            invoker = null;
        }

        AbstractAction swingAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (invoker!=null) {
                    try {
                        invoker.invoke(ActionEvent.class, e);
                    } catch (Exception e1) {
                        logger.error(toString()+": can't invoke method '"+getOnAction()+"': "+e1.getMessage());
                    }
                }
            }
        };
        return swingAction;
    }


    @Override
    public void afterComponentsConverted() {
        super.afterComponentsConverted();
        //we need to resolve placeholders here, because the were didn't resolved during 'parse head ' phase:
        // ui:loadBundle was not invoked.
        javax.swing.Action swingAction = getModel().getActions().get(getActionName());
        if (StringUtils.isNotEmpty(getTitle())) {
            swingAction.putValue(javax.swing.Action.NAME, ELUtils.parseStringValue(getTitle(), getModelElements()));
        }
        if (StringUtils.isNotEmpty(getIcon())) {
            Icon icon = null;
            try {
                String val = ELUtils.parseStringValue(getIcon(), getModelElements());
                InputStream in = getModel().getConfiguration().getResourceLoader().loadResource(getModel(), val);
                Image image = Toolkit.getDefaultToolkit().createImage(IOUtils.toByteArray(in));
                /*Image image = ImageIO.read(getModel().getConfiguration().getResourceLoader().loadResource(getModel(), val));*/
                icon = new ImageIcon(image);
            } catch (Exception e) {
                logger.warn("Can't load icon from resource '"+getIcon()+"': "+e.getMessage());
            }

            if (icon!=null) {
                swingAction.putValue(javax.swing.Action.SMALL_ICON, icon);
            }
        }

    }
}


