package ru.swing.html.tags.swing;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.ELUtils;
import ru.swing.html.IOUtils;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;

/**
 * Is converted to JMenuItem
 */
public class MenuItem extends Tag {

    private Log logger = LogFactory.getLog(getClass());
    private String actionName;
    private String title;
    private String icon;
    private JComponent popupTarget;
    private javax.swing.Action bindedAction;
    private BindedActionListener bindedActionListener = new BindedActionListener();

    @Override
    public void handleLayout() {
    }

    @Override
    public JComponent createComponent() {
        return new JMenuItem();
    }


    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public void applyAttribute(JComponent component, String attrName) {
        JMenuItem menuItem = (JMenuItem) component;
        if ("actionname".equals(attrName)) {


            //as we create wrapper for action, we need to bind some properties from original
            //action with wrapper
            if (bindedAction!=null) {
                bindedAction.removePropertyChangeListener(bindedActionListener);
            }

            String actionName = getActionName();
            final javax.swing.Action swingAction = getModel().getActions().get(actionName);

            final javax.swing.Action wrapper = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    if (popupTarget != null) {
                        e.setSource(getPopupTarget());
                    }
                    swingAction.actionPerformed(e);
                }
            };
            wrapper.putValue(javax.swing.Action.ACCELERATOR_KEY, swingAction.getValue(javax.swing.Action.ACCELERATOR_KEY));
            wrapper.putValue(javax.swing.Action.ACTION_COMMAND_KEY, swingAction.getValue(javax.swing.Action.ACTION_COMMAND_KEY));
            wrapper.putValue(javax.swing.Action.DEFAULT, swingAction.getValue(javax.swing.Action.DEFAULT));
            wrapper.putValue(javax.swing.Action.LONG_DESCRIPTION, swingAction.getValue(javax.swing.Action.LONG_DESCRIPTION));
            wrapper.putValue(javax.swing.Action.MNEMONIC_KEY, swingAction.getValue(javax.swing.Action.MNEMONIC_KEY));
            wrapper.putValue(javax.swing.Action.NAME, StringUtils.isNotEmpty(getTitle()) ? getTitle() : swingAction.getValue(javax.swing.Action.NAME));
            wrapper.putValue(javax.swing.Action.SHORT_DESCRIPTION, swingAction.getValue(javax.swing.Action.SHORT_DESCRIPTION));
            wrapper.putValue(javax.swing.Action.SMALL_ICON, StringUtils.isNotEmpty(getIcon()) ? loadIcon() : swingAction.getValue(javax.swing.Action.SMALL_ICON));

            bindedAction = swingAction;
            bindedAction.addPropertyChangeListener(bindedActionListener);

            menuItem.setAction(wrapper);
        } else if ("icon".equals(attrName)) {

            Icon icon = loadIcon();

            if (menuItem.getAction()!=null) {
                menuItem.getAction().putValue(javax.swing.Action.SMALL_ICON, icon);
            }
            else {
                menuItem.setIcon(icon);
            }


        } else if ("title".equals(attrName)) {
            if (menuItem.getAction()!=null) {
                menuItem.getAction().putValue(javax.swing.Action.NAME, getTitle());
            }
            else {
                menuItem.setText(getTitle());
            }
        } else {
            super.applyAttribute(component, attrName);
        }
    }

    private Icon loadIcon() {
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
        return icon;
    }

    @Override
    public void setAttribute(String name, String value) {
        if ("actionname".equals(name)) {
            setActionName(value);
        }
        else if ("title".equals(name)) {
            setTitle(value);
        }
        else if ("icon".equals(name)) {
            setIcon(value);
        }
        super.setAttribute(name, value);
    }


    public void setPopupTarget(JComponent popupTarget) {
        this.popupTarget = popupTarget;
    }

    public JComponent getPopupTarget() {
        return popupTarget;
    }

    /**
     * This class will keep original action from dom model in sync with JMenuItem.
     */
    private class BindedActionListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            JMenuItem menuItem = (JMenuItem) getComponent();
            if ("enabled".equals(evt.getPropertyName())) {
                menuItem.setEnabled((Boolean) evt.getNewValue());
            }
            else if (javax.swing.Action.ACCELERATOR_KEY.equals(evt.getPropertyName())) {
                menuItem.getAction().putValue(javax.swing.Action.ACCELERATOR_KEY, evt.getNewValue());
            }
            else if (javax.swing.Action.ACTION_COMMAND_KEY.equals(evt.getPropertyName())) {
                menuItem.getAction().putValue(javax.swing.Action.ACTION_COMMAND_KEY, evt.getNewValue());
            }
            else if (javax.swing.Action.LONG_DESCRIPTION.equals(evt.getPropertyName())) {
                menuItem.getAction().putValue(javax.swing.Action.LONG_DESCRIPTION, evt.getNewValue());
            }
            else if (javax.swing.Action.MNEMONIC_KEY.equals(evt.getPropertyName())) {
                menuItem.getAction().putValue(javax.swing.Action.MNEMONIC_KEY, evt.getNewValue());
            }
            //check name is not overriden
            else if (javax.swing.Action.NAME.equals(evt.getPropertyName()) && StringUtils.isEmpty(getTitle())) {
                menuItem.getAction().putValue(javax.swing.Action.NAME, evt.getNewValue());
            }
            else if (javax.swing.Action.SHORT_DESCRIPTION.equals(evt.getPropertyName())) {
                menuItem.getAction().putValue(javax.swing.Action.SHORT_DESCRIPTION, evt.getNewValue());
            }
            //check icon is not overriden
            else if (javax.swing.Action.SMALL_ICON.equals(evt.getPropertyName()) && StringUtils.isNotEmpty(getIcon())) {
                menuItem.getAction().putValue(javax.swing.Action.SMALL_ICON, evt.getNewValue());
            }
        }
    }
}
