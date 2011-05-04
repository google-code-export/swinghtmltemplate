package ru.swing.html.tags.ui;

/**
 * Special class for containing current iteration status inside ForEach tag.
 * @see ForEach
*/
public class VarStatus {

    private int index;
    private boolean last;
    private boolean first;

    /**
     * Returns current iteraion index
     * @return index
     */
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Returns wheter current item is last in the collection
     * @return is this item is last
     */
    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    /**
     * Returns wheter current item is first in the collection
     * @return is this item is first
     */
    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }
}
