package ru.swing.html.tags.ui;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;
import ru.swing.html.tags.Tag;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Use this tag to include target document to initial source document.
 *
 * The childs of target's body tag are included, so they will be the direct child tags of the include's parent tag.
 *
 * Example:
 *
 * source:
 * <pre>
 * &lt;html&gt;
 * &lt;body&gt;
 *   &lt;div&gt;
 *     &lt;include src='ru/test/target.html'&gt;
 *   &lt;/div&gt;
 * &lt;/body&gt;
 * &lt;html&gt;
 * </pre>
 *
 * target:
 * <pre>
 * &lt;html&gt;
 * &lt;body&gt;
 *   &lt;input type='button'/&gt;
 * &lt;/body&gt;
 * &lt;html&gt;
 * </pre>
 *
 * result:
 * <pre>
 * &lt;html&gt;
 * &lt;body&gt;
 *   &lt;div&gt;
 *     &lt;input type='button'/&gt;
 *   &lt;/div&gt;
 * &lt;/body&gt;
 * &lt;html&gt;
 * </pre>
 */
public class Include extends Tag {

    private Log logger = LogFactory.getLog(getClass());
    private String source;

    @Override
    public void afterChildElementsConverted() {
        if (StringUtils.isEmpty(getSource())) {
            logger.warn(toString()+": 'src' attribute is required.");
            return;
        }

        //loading target document model
        InputStream in = getClass().getClassLoader().getResourceAsStream(getSource());
        DomModel target = null;
        try {
            target = DomLoader.loadModel(in);
        } catch (JDOMException e) {
            logger.error(toString() + ": Can't include document: " + getSource(), e);
        } catch (IOException e) {
            logger.error(toString() + ": Can't include document: " + getSource(), e);
        }


        if (target==null) {
            return;
        }

        //merge target dom-model with source dom

        //find target's body tag
        Tag body;
        Tag[] res = target.query("body");
        if (res==null || res.length!=1) {
            logger.error(toString() + ": Can't find body of included document: " + getSource());
            return;
        }

        body = res[0];

        //append all body's children
        getModel().mergeTag(this, body);

    }




    @Override
    public void setAttribute(String name, String value) {
        super.setAttribute(name, value);
        if ("src".equals(name)) {
            setSource(value);
        }
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
