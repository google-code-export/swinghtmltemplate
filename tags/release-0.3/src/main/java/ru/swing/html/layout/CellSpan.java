package ru.swing.html.layout;

/**
 * Вспомогательный класс для хранения информации о растяжении ячейки таблицы в tableLayout.
 */
public class CellSpan {

    private int row;
    private int column;
    private int rowSpan;
    private int columnSpan;

    public CellSpan(int row, int column, int rowSpan, int columnSpan) {
        this.row = row;
        this.column = column;
        this.rowSpan = rowSpan;
        this.columnSpan = columnSpan;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public int getColumnSpan() {
        return columnSpan;
    }
}
