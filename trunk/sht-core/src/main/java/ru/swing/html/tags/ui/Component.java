package ru.swing.html.tags.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.*;
import org.jdom.JDOMException;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;
import ru.swing.html.ELUtils;
import ru.swing.html.tags.Body;
import ru.swing.html.tags.Tag;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * This tag allows to define components composition in external file. When used this tag parses 
 * this external file and adds external body's content to the original document in place of itself.
 * </p>
 * <p>
 *     Use 'src' attribute to locate target document.
 * </p>
 * <h2>Example</h2>
 * /src/foo/original.html
 * <pre>
 *     &lt;html>
 *         &lt;body style='display:box;'>
 *             &lt;ui:forEach items='${model.persons}' var="person">
 *                 &lt;ui:component src='foo/personPanel.html' person="${person}"/>
 *             &lt;/ui:forEach>
 *         &lt;/body>
 *     &lt;/html>
 * </pre>
 * 
 * /src/foo/personPanel.html
 * <pre>
 *     &lt;html>
 *         &lt;body style='display:box;'>
 *             &lt;table>
 *                 &lt;tr>
 *                     &lt;td>&lt;p>Name:&lt;/p>&lt;/td>
 *                     &lt;td>&lt;input type='text' value='${person.name}'/>&lt;/td>
 *                 &lt;/tr>
 *             &lt;/table>
 *         &lt;/body>
 *     &lt;/html>
 * </pre>
 *
 * This results in the following html (assume we have 2 persons in our ${model.persons}, Mary and Bill)
 * <pre>
 *     &lt;html>
 *         &lt;body style='display:box;'>
 *             &lt;table>
 *                 &lt;tr>
 *                     &lt;td>&lt;p>Name:&lt;/p>&lt;/td>
 *                     &lt;td>&lt;input type='text' value='Mary'/>&lt;/td>
 *                 &lt;/tr>
 *             &lt;/table>
 *             &lt;table>
 *                 &lt;tr>
 *                     &lt;td>&lt;p>Name:&lt;/p>&lt;/td>
 *                     &lt;td>&lt;input type='text' value='Bill'/>&lt;/td>
 *                 &lt;/tr>
 *             &lt;/table>
 *         &lt;/body>
 *     &lt;/html>
 * </pre>
 *
 * <p>
 *     As you can see, we exposed var, 'person' to our component. Every attribute (besides src) is treates as EL
 *     to expose to component.
 * </p>
 *
 */
public class Component extends Tag {

    private Log logger = LogFactory.getLog(getClass());
    private String source;


    @Override
    public void beforeComponentsConvertion() {

        //loading target document model
//        InputStream in = getClass().getClassLoader().getResourceAsStream(getSource());
        DomModel targetModel = null;
        String url = ELUtils.parseStringValue(getSource(), getModelElements());
        try {
            InputStream in = getModel().getConfiguration().getResourceLoader().loadResource(getModel(), url);
            targetModel = DomLoader.loadModel(in);
        } catch (JDOMException e) {
            logger.error(toString() + ": Can't load document: " + url, e);
        } catch (IOException e) {
            logger.error(toString() + ": Can't load document: " + url, e);
        }

        if (targetModel ==null) {
            return;
        }

        Body target = (Body) targetModel.getRootTag().getChildByName("body");

        if (target!=null) {

            BindingGroup group = new BindingGroup();
            for (String attrName : getAttributes().keySet()) {
                if (!"src".equals(attrName) && !TAG_CONTENT.equals(attrName)) {
                    ELProperty sourceProp = ELProperty.create(getAttribute(attrName));
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
