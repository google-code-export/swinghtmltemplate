package ru.swing.html.tags.swing;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.ELProperty;
import ru.swing.html.Utils;
import ru.swing.html.tags.Tag;
import ru.swing.html.tags.event.MouseListenerClickDelegator;
import ru.swing.html.tags.event.TreeSelectionDelegator;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.*;
import java.lang.Object;
import java.lang.reflect.Method;

/**
 * JTree
 */
public class Tree extends Tag {

    private Log logger = LogFactory.getLog(getClass());
    private boolean showRoot = true;
    private boolean showRootHandles = false;
    private String renderer;
    private TreeSelectionDelegator treeModelDelegator;
    private MouseListenerClickDelegator mouseListenerClickDelegator;
    private MouseListenerClickDelegator mouseListenerDblClickDelegator;

    @Override
    public JComponent createComponent() {

        JTree tree = new JTree();
        setComponent(tree);
        return tree;
    }

    @Override
    public void applyAttributes(JComponent component) {
        super.applyAttributes(component);
        JTree tree = (JTree) getComponent();

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


        tree.setRootVisible(isShowRoot());
        tree.setShowsRootHandles(isShowRootHandles());

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



        //install model selection listener
        final String onchangeMethod = getAttribute("onchange");
        if (StringUtils.isNotEmpty(onchangeMethod)) {

            Object controller = getModel().getController();
            Method method = Utils.findActionMethod(controller.getClass(), onchangeMethod, TreeSelectionEvent.class);
            //если метод нашелся, то добавляем к компоненту слушатель, который вызывает метод.
            if (method!=null) {
                //добавляем слушатель, который вызывает метод
                if (treeModelDelegator !=null) {
                    tree.getSelectionModel().removeTreeSelectionListener(treeModelDelegator);
                }
                treeModelDelegator = new TreeSelectionDelegator(controller, method);
            }
            else {
                logger.warn(toString()+ ": can't find method " + onchangeMethod + " in class " +controller.getClass().getName());
            }


            tree.getSelectionModel().addTreeSelectionListener(treeModelDelegator);
        }

        //install click listener
        final String onclickMethod = getAttribute("onclick");
        if (StringUtils.isNotEmpty(onclickMethod)) {

            Object controller = getModel().getController();
            Method method = Utils.findActionMethod(controller.getClass(), onclickMethod, MouseEvent.class);
            //если метод нашелся, то добавляем к компоненту слушатель, который вызывает метод.
            if (method!=null) {
                //добавляем слушатель, который вызывает метод
                if (mouseListenerClickDelegator !=null) {
                    tree.removeMouseListener(mouseListenerClickDelegator);
                }
                mouseListenerClickDelegator = new MouseListenerClickDelegator(controller, method, 1);
            }
            else {
                logger.warn(toString()+ ": can't find method " + onchangeMethod + " in class " +controller.getClass().getName());
            }


            tree.addMouseListener(mouseListenerClickDelegator);
        }

        //install dblclick listener
        final String ondblclickMethod = getAttribute("ondblclick");
        if (StringUtils.isNotEmpty(ondblclickMethod)) {

            Object controller = getModel().getController();
            Method method = Utils.findActionMethod(controller.getClass(), ondblclickMethod, MouseEvent.class);
            //если метод нашелся, то добавляем к компоненту слушатель, который вызывает метод.
            if (method!=null) {
                //добавляем слушатель, который вызывает метод
                if (mouseListenerDblClickDelegator !=null) {
                    tree.removeMouseListener(mouseListenerDblClickDelegator);
                }
                mouseListenerDblClickDelegator = new MouseListenerClickDelegator(controller, method, 2);
            }
            else {
                logger.warn(toString()+ ": can't find method " + ondblclickMethod + " in class " +controller.getClass().getName());
            }


            tree.addMouseListener(mouseListenerDblClickDelegator);
        }

    }

    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);

        if ("showroot".equals(name)) {
            setShowRoot(Boolean.valueOf(value));
        }
        else if ("showroothandles".equals(name)) {
            setShowRootHandles(Boolean.valueOf(value));
        }
        else if ("renderer".equals(name)) {
            setRenderer(value);
        }

    }

    public boolean isShowRoot() {
        return showRoot;
    }

    public void setShowRoot(boolean showRoot) {
        this.showRoot = showRoot;
    }

    public boolean isShowRootHandles() {
        return showRootHandles;
    }

    public void setShowRootHandles(boolean showRootHandles) {
        this.showRootHandles = showRootHandles;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }
}
