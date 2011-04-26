package ru.swing.html.components;

/**
* Helper class to hold table cell coordinates.
*/
public class Pair {
    private int column, row;

    Pair(int row, int column) {
        this.column = column;
        this.row = row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair pair = (Pair) o;

        if (column != pair.column) return false;
        if (row != pair.row) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = column;
        result = 31 * result + row;
        return result;
    }
}
