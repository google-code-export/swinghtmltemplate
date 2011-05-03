package ru.swing.html.tags.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.*;
import org.jdom.JDOMException;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;
import ru.swing.html.tags.Body;
import ru.swing.html.tags.Tag;

import java.io.IOException;
import java.io.InputStream;

/**
 */
public class Component extends Tag {

    private Log logger = LogFactory.getLog(getClass());
    private String source;


    @Override
    public void beforeComponentsConvertion() {

        //loading target document model
        InputStream in = getClass().getClassLoader().getResourceAsStream(getSource());
        DomModel targetModel = null;
        try {
            targetModel = DomLoader.loadModel(in);
        } catch (JDOMException e) {
            logger.error(toString() + ": Can't load document: " + getSource(), e);
        } catch (IOException e) {
            logger.error(toString() + ": Can't load document: " + getSource(), e);
        }

        if (targetModel ==null) {
            return;
        }

        Body target = (Body) targetModel.getRootTag().getChildByName("body");

        if (target!=null) {

            BindingGroup group = new BindingGroup();
            for (String attrName : getAttributes().keySet()) {
                if (!"src".equals(attrName)) {
                    ELProperty sourceProp = ELProperty.create(getAttributes().get(attrName));
                    BeanProperty targetProp = BeanProperty.create(attrName);

                    for (Tag targetChild : target.getChildren()) {

                        Binding binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
                                getModelElements(),
                                sourceProp,
                                targetChild.getModelElements(),
                                targetProp);
                        group.addBinding(binding);
                    }
                }
            }

            getModel().mergeTag(this, target);
            group.bind();

        }



    }

    @Override
    public void setAttribute(String name, String value) {
        if ("src".equals(name)) {
            setSource(value);
        }
        super.setAttribute(name, value);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
