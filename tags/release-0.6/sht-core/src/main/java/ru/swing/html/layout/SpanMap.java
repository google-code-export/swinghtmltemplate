package ru.swing.html.layout;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for storing list of spanned cells in tableLayout.
 */
public class SpanMap {

    private List<CellSpan> spans = new ArrayList<CellSpan>();

    public void addSpan(CellSpan span) {
        spans.add(span);
    }

    public CellSpan getSpanAtLocation(int row, int column) {
        for (CellSpan sp : spans) {
            if (row==sp.getRow() && column==sp.getColumn()) {
                return sp;
            }
        }
        return null;
    }

    public CellSpan getSpanForCell(int row, int column) {
        for (CellSpan sp : spans) {
            if (row>=sp.getRow() && row<sp.getRow()+sp.getRowSpan() &&
                    column>=sp.getColumn() && column<sp.getColumn()+sp.getColumnSpan()) {
                return sp;
            }
        }
        return null;
    }

}
