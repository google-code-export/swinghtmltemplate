package ru.swing.html.tags.swing;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.ELProperty;
import ru.swing.html.ELUtils;
import ru.swing.html.Utils;
import ru.swing.html.configuration.MethodInvoker;
import ru.swing.html.tags.Tag;
import ru.swing.html.tags.event.MouseListenerClickDelegator;
import ru.swing.html.tags.event.TreeSelectionDelegator;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;

/**
 * <p>The tag is converted to `javax.swing.JTree` component.</p>
 *
 * <p>Supported attributes:</p>
 * <ul>
 *     <li>value - EL, pointing to `javax.swing.tree.TreeModel` instance which will be used as tree model
 *     <li>showRoot - show tree root or not. Values: `true` or `false`
 *     <li>showRootHandles - show tree root handles or not. Values: `true` or `false`
 * </ul>
 *
 * <p>The tag supports events:</p>
 * <ul>
 *     <li><strong>onchange</strong> - the name of the controller's method to invoke on selection change.
 *     The method must take no arguments or take 1 argument of type `javax.swing.event.TreeSelectionEvent`
 *
 *     <li><strong>onclick</strong> - the name of the controller's method to invoke tree click.
 *     The method must take no arguments or take 1 argument of type `javax.swing.event.MouseEvent`
 *
 *     <li><strong>ondblclick</strong> - the name of the controller's method to invoke on tree double click.
 *     The method must take no arguments or take 1 argument of type `javax.swing.event.MouseEvent`
 * </ul>
 *
 * <h2>Example:</h2>
 * <pre>
 * &lt;c:scroll>
 *     &lt;c:tree value="${categoriesTreeModel}" showRoot="false" onchange="onCategoryChange" showRootHandles="true"/>
 * &lt;/c:scroll>
 * </pre>
 *
 */
public class Tree extends Tag {

    private Log logger = LogFactory.getLog(getClass());
    private String showRoot;
    private String showRootHandles;
    private String renderer;
    private TreeSelectionDelegator treeModelDelegator;
    private MouseListenerClickDelegator mouseListenerClickDelegator;
    private MouseListenerClickDelegator mouseListenerDblClickDelegator;

    @Override
    public JComponent createComponent() {
        return new JTree();
    }

    @Override
    public void applyAttribute(JComponent component, String name) {
        JTree tree = (JTree) getComponent();


        if ("value".equals(name)) {
            String modelEL = getAttribute("value");
            if (StringUtils.isNotEmpty(modelEL)) {
                ELProperty beanProperty = ELProperty.create(modelEL);

                java.lang.Object propertyValue = beanProperty.getValue(getModel().getModelElements());
                if (!(propertyValue instanceof TreeModel)) {
                    logger.warn(toString()+": value must be binded to the property of type "+TreeModel.class.getName());
                    return;
                }


                tree.setModel((TreeModel) propertyValue);
                logger.trace(toString()+" set tree model from el: "+modelEL);
            }
        }
        else if ("showroot".equals(name)) {
            Boolean v = Boolean.parseBoolean(ELUtils.parseStringValue(getShowRoot(), getModelElements()));
            tree.setRootVisible(v);
        }
        else if ("showroothandles".equals(name)) {
            Boolean v = Boolean.parseBoolean(ELUtils.parseStringValue(getShowRootHandles(), getModelElements()));
            tree.setShowsRootHandles(v);
        }
        else if ("renderer".equals(name)) {
            //install renderer
            String rendererAttr = getRenderer();
            if (StringUtils.isNotEmpty(rendererAttr)) {
                ELProperty rendererProperty = ELProperty.create(rendererAttr);
                Object rendererVal = rendererProperty.getValue(getModel().getModelElements());
                if (renderer == null) {
                    logger.warn(toString()+ ": can't set renderer. Object '"+rendererAttr + "' is null");
                }
                else if (rendererVal instanceof TreeCellRenderer) {
                    tree.setCellRenderer((TreeCellRenderer) rendererVal);
                    logger.trace(toString() + ": set renderer: '" + rendererAttr + "'");
                }
                else {
                    logger.warn(toString()+ ": can't set renderer. Object '"+rendererAttr + "' is not instance of "+TreeCellRenderer.class.getName());
                }

            }

        }
        else if ("onchange".equals(name)) {
            //install model selection listener
            final String onchangeMethod = getAttribute("onchange");
            if (StringUtils.isNotEmpty(onchangeMethod)) {

                MethodInvoker invoker = getModel().getConfiguration().getMethodResolverService().resolveMethod(onchangeMethod, this);
                //if invoker is found
                if (invoker!=null) {
                    //create delegator
                    if (treeModelDelegator !=null) {
                        tree.getSelectionModel().removeTreeSelectionListener(treeModelDelegator);
                    }
                    treeModelDelegator = new TreeSelectionDelegator(invoker);
                }
                else {
                    logger.warn(toString()+ ": can't find method invoker for '" + onchangeMethod + "'");
                }


                tree.getSelectionModel().addTreeSelectionListener(treeModelDelegator);
            }
        }
        else if ("onclick".equals(name)) {
            //install click listener
            final String onclickMethod = getAttribute("onclick");
            if (StringUtils.isNotEmpty(onclickMethod)) {

                MethodInvoker invoker = getModel().getConfiguration().getMethodResolverService().resolveMethod(onclickMethod, this);
                //if the invoker is found
                if (invoker!=null) {
                    //добавляем слушатель, который вызывает метод
                    if (mouseListenerClickDelegator !=null) {
                        tree.removeMouseListener(mouseListenerClickDelegator);
                    }
                    mouseListenerClickDelegator = new MouseListenerClickDelegator(invoker, 1);
                }
                else {
                    logger.warn(toString()+ ": can't find method invoker for '" + onclickMethod + "'");
                }


                tree.addMouseListener(mouseListenerClickDelegator);
            }
        }
        else if ("ondblclick".equals(name)) {
            //install dblclick listener
            final String ondblclickMethod = getAttribute("ondblclick");
            if (StringUtils.isNotEmpty(ondblclickMethod)) {

                MethodInvoker invoker = getModel().getConfiguration().getMethodResolverService().resolveMethod(ondblclickMethod, this);
                //if invoker is found
                if (invoker!=null) {
                    //add delegator
                    if (mouseListenerDblClickDelegator !=null) {
                        tree.removeMouseListener(mouseListenerDblClickDelegator);
                    }
                    mouseListenerDblClickDelegator = new MouseListenerClickDelegator(invoker, 2);
                }
                else {
                    logger.warn(toString()+ ": can't find method invoker for '" + ondblclickMethod + "'");
                }


                tree.addMouseListener(mouseListenerDblClickDelegator);
            }
        }
        else {
            super.applyAttribute(component, name);
        }
    }

    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);

        if ("showroot".equals(name)) {
            setShowRoot(value);
        }
        else if ("showroothandles".equals(name)) {
            setShowRootHandles(value);
        }
        else if ("renderer".equals(name)) {
            setRenderer(value);
        }

    }

    public String getShowRoot() {
        return showRoot;
    }

    public void setShowRoot(String showRoot) {
        this.showRoot = showRoot;
    }

    public String getShowRootHandles() {
        return showRootHandles;
    }

    public void setShowRootHandles(String showRootHandles) {
        this.showRootHandles = showRootHandles;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }
}
