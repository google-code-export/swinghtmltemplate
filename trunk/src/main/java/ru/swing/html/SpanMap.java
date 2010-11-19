package ru.swing.html;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 19.11.2010
 * Time: 23:02:56
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
