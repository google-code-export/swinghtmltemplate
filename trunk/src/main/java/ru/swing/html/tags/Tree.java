package ru.swing.html.tags;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.ELProperty;
import ru.swing.html.Utils;
import ru.swing.html.tags.event.TreeSelectionDelegator;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
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
    private TreeSelectionDelegator treeModelDelegator;

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
}
