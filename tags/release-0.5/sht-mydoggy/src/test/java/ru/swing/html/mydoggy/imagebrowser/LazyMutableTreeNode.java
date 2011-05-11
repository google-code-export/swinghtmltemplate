package ru.swing.html.mydoggy.imagebrowser;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * http://www.java2s.com/Open-Source/Java-Document/Swing-Library/jide-common/com/jidesoft/plaf/basic/LazyMutableTreeNode.java.htm
 */
public abstract class LazyMutableTreeNode extends DefaultMutableTreeNode {

    protected boolean _loaded = false;

    public LazyMutableTreeNode() {
    }

    public LazyMutableTreeNode(Object userObject) {
        super(userObject);
    }

    public LazyMutableTreeNode(Object userObject, boolean allowsChildren) {
        super(userObject, allowsChildren);
    }


    @Override
    public int getChildCount() {
        synchronized (this) {
            if (!_loaded) {
                _loaded = true;
                initChildren();
            }
        }
        return super.getChildCount();
    }

    public void clear() {
        removeAllChildren();
        _loaded = false;
    }

    public boolean isLoaded() {
        return _loaded;
    }


    protected abstract void initChildren();
}
