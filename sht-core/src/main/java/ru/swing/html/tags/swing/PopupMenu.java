package ru.swing.html.tags.swing;

import org.apache.commons.lang.StringUtils;
import ru.swing.html.DomConverter;
import ru.swing.html.ELUtils;
import ru.swing.html.TagVisitor;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Hr;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

public class PopupMenu extends Tag {

    private String forAttribute;
    private String nested;


    private List<Tag> elements;

    /**
     * Holds the component, on which popupTrigger event occured. This is used to set
     * event source when invoking action. So, if you right click on input, then
     * on controller you can get this input via action.getSource();
     */
    private JComponent popupTarget;

    @Override
    public JComponent createComponent() {
        return new JPopupMenu();
    }

    @Override
    public void handleLayout() {
    }

    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {

        JPopupMenu popupMenu = (JPopupMenu) getComponent();


        Boolean nest = Boolean.valueOf(ELUtils.parseStringValue(nested, getModelElements())) && StringUtils.isEmpty(getFor());

        elements = new ArrayList<Tag>(getChildren());

        //if in nested mode, collect all parent menu items
        if (nest) {

            Tag parent = getParent();
            //move to upper grandparent.
            if (parent != null) {
                parent = parent.getParent();
            }
            //search for parent popup menus
            while (parent != null) {
                List<Tag> parentChilds = parent.getChildren();
                for (Tag parentTag : parentChilds) {
                    if (parentTag instanceof PopupMenu) {
                        PopupMenu parentMenu = (PopupMenu) parentTag;
                        if (StringUtils.isEmpty(parentMenu.getFor()) || new SelectorGroup(parentMenu.getFor()).matches(getParent())) {
                            elements.add(new Hr());//place separator between parent menus
                            for (Tag parentItem : parentMenu.getChildren()) {
                                elements.add(parentItem);
                            }
                        }
                    }
                }
                parent = parent.getParent();
            }
        }

        for (Tag childTag : elements) {
            if (childTag instanceof MenuItem) {
                JMenuItem child = (JMenuItem) DomConverter.convertComponent(childTag, substitutions);
                popupMenu.add(child);
            } else if (childTag instanceof Menu) {
                JComponent child = DomConverter.convertComponent(childTag, substitutions);
                popupMenu.add(child);
            } else if (childTag instanceof Hr) {
                popupMenu.addSeparator();
            }
        }
    }

    public String getFor() {
        return forAttribute;
    }

    public void setFor(String forAttribute) {
        this.forAttribute = forAttribute;
    }

    public String getNested() {
        return nested;
    }

    public void setNested(String nested) {
        this.nested = nested;
    }

    @Override
    public void setAttribute(String name, String value) {
        if ("for".equals(name)) {
            setFor(value);
        } else if ("nested".equals(name)) {
            setNested(value);
        }
        super.setAttribute(name, value);
    }


    @Override
    public void afterComponentsConverted() {

        final JPopupMenu menu = (JPopupMenu) getComponent();

        if (StringUtils.isNotEmpty(getFor())) {
            getModel().query(getFor()).each(new TagVisitor() {
                public void visit(Tag tag) {
                    if (tag.getComponent() != null) {
                        final JComponent target = tag.getComponent();
                        PopupTrigger popupTrigger = new PopupTrigger(target, menu, getDepth());
                        replaceTrigger(target, popupTrigger);
                    }
                }
            });
        } else {
            Tag tag = getParent();
            if (tag.getComponent() != null) {
                final JComponent target = tag.getComponent();
                //remove any global popup triggers (if any), so direct popupMenu will have highest priority
                //install popup trigger
                PopupTrigger popupTrigger = new PopupTrigger(target, menu, Integer.MIN_VALUE);
                replaceTrigger(target, popupTrigger);
            }
        }
    }

    public void setPopupTarget(JComponent popupTarget) {
        this.popupTarget = popupTarget;
        for (Tag child : elements) {
            if (child instanceof Menu) {
                ((Menu) child).setPopupTarget(popupTarget);
            }
            else if (child instanceof MenuItem) {
                ((MenuItem) child).setPopupTarget(popupTarget);
            }
        }
    }

    /**
     * Counts the depth of the tag inside the tags tree
     * @return depth
     */
    private int getDepth() {
        int res = 0;
        Tag parent = getParent();
        while (parent!=null) {
            res++;
            parent = parent.getParent();
        }
        return res;
    }

    /**
     * Remove all mouse listeners of class PopupTrigger from component that have depth bigger then the depth
     * of installed trigger. Adds trigger as mouse listener if there is no listners with lesser depth.
     * @param target component, from which listeners are removed
     * @param installed new trigger to install if it has the smallest depth
     */
    private void replaceTrigger(JComponent target, PopupTrigger installed) {
        List<MouseListener> toRemove = new ArrayList<MouseListener>();
        int minDepth = Integer.MAX_VALUE;
        for (MouseListener listener : target.getMouseListeners()) {
            if (listener instanceof PopupTrigger) {
                PopupTrigger trigger = (PopupTrigger) listener;
                if (installed.depth<=trigger.depth) {
                    toRemove.add(listener);
                }
                else {
                    minDepth = trigger.depth;
                }
            }
        }
        for (MouseListener rem : toRemove) {
            target.removeMouseListener(rem);
        }
        if (installed.depth<minDepth) {
            target.addMouseListener(installed);
        }
    }

    private class PopupTrigger extends MouseAdapter {
        private final JComponent target;
        private final JPopupMenu menu;
        private int depth = 0;

        public PopupTrigger(JComponent target, JPopupMenu menu, int depth) {
            this.target = target;
            this.menu = menu;
            this.depth = depth;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
                setPopupTarget(target);
                menu.show(target, e.getX(), e.getY());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                setPopupTarget(target);
                menu.show(target, e.getX(), e.getY());
            }
        }
    }
}
