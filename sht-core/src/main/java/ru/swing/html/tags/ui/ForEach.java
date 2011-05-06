package ru.swing.html.tags.ui;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.ELProperty;
import ru.swing.html.DomConverter;
import ru.swing.html.TagVisitor;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * This tag allows to iterate over some iteratable collection. The child tags will be
 * duplicated for each item in collection.
 * </p>
 * <p>
 *     Supported attributes:
 *     <ul>
 *         <li>var - the name of exposed variable, which will be available in child tags,
 *         containing current collection item</li>
 *         <li>items - the EL, pointing to the iteratable collection</li>
 *         <li>varStatus - the name of exposed variable, which will be available inside child tags. It will contain
 *         current iteration status</li>
 *     </ul>
 * </p>
 * @see VarStatus
 */
public class ForEach extends Tag {

    public String var;
    public String items;
    public String varStatus;

    private Log logger = LogFactory.getLog(getClass());

    @Override
    public JComponent createComponent() {
        return null;
    }

    @Override
    public void handleLayout() {
    }


    @Override
    public void beforeComponentsConvertion() {
        super.beforeComponentsConvertion();


        ELProperty<Map, Object> prop = ELProperty.create(getItems());
        Object raw = prop.getValue(getModelElements());


        if (raw == null) {
            logger.warn(toString()+": value of '"+getItems()+"' el resolved to null");
        }
        else if (raw instanceof Iterable) {

            Tag parent = getParent();
            int tagIndex = getParent().getChildren().indexOf(this);
            getParent().removeChild(this);

            List<Tag> children = new ArrayList<Tag>();
            children.addAll(getChildren());

            for (Tag child : children) {
                removeChild(child);
            }

            Iterable items =  (Iterable) raw;


            Iterator it = items.iterator();
            int index = 0;
            boolean first = true;
            while (it.hasNext()) {
                Object item = it.next();

                VarStatus status = new VarStatus();
                status.setFirst(first);
                status.setLast(!it.hasNext());
                status.setIndex(index);

                for (Tag child : children) {
                    final Tag childClone = child.clone();
                    parent.addChild(childClone, tagIndex++);
                    childClone.addModelElement(getVar(), item);
                    if (StringUtils.isNotEmpty(getVarStatus())) {
                        childClone.addModelElement(getVarStatus(), status);
                    }
                    DomConverter.recursivellyVisitTags(childClone, new TagVisitor() {
                        public void visit(Tag tag) {
                            if (!childClone.equals(tag)) {
                                tag.createContextModel();
                            }
                        }
                    });
                }

                first = false;
                index++;
            }
        }
        else {
            logger.warn(toString()+": value of '"+getItems()+"' el must be instance of "+Iterable.class);
        }

    }


    @Override
    public void setAttribute(String name, String value) {
        if ("var".equals(name)) {
            setVar(value);
        }
        else if ("varstatus".equals(name)) {
            setVarStatus(value);
        }
        else if ("items".equals(name)) {
            setItems(value);
        }
        super.setAttribute(name, value);
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getVarStatus() {
        return varStatus;
    }

    public void setVarStatus(String varStatus) {
        this.varStatus = varStatus;
    }

}
