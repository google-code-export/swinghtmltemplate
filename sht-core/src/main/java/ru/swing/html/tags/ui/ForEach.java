package ru.swing.html.tags.ui;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.el.impl.parser.ELParser;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 29.04.11
 * Time: 12:05
 */
public class ForEach extends Tag {

    public String var;
    public String items;
    public String varStatus;

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
        Object raw = prop.getValue(getModel().getModelElements());


        if (raw instanceof Iterable) {

            Tag parent = getParent();
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
                status.setLast(it.hasNext());
                status.setIndex(index);

                for (Tag child : children) {
                    Tag childClone = child.clone();
                    parent.addChild(childClone);
                    childClone.addModelElement(getVar(), item);
                    childClone.addModelElement(getVarStatus(), status);
                }

                first = false;
                index++;
            }
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

    public class VarStatus {

        private int index;
        private boolean last;
        private boolean first;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public boolean isLast() {
            return last;
        }

        public void setLast(boolean last) {
            this.last = last;
        }

        public boolean isFirst() {
            return first;
        }

        public void setFirst(boolean first) {
            this.first = first;
        }
    }
}
