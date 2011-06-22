package ru.swing.html.sandbox.dnd;

import ru.swing.html.configuration.AttributeParser;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.io.IOException;

public class DragSourceAttributeParser implements AttributeParser {


    public static DataFlavor tagDataVlavor;
    private TransferDataProvider transferDataProvider = new TagTransferDataProvider();

    static {
        try {
            tagDataVlavor = new DataFlavor("sht/tag-source");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void applyAttribute(final Tag tag, JComponent component, String attrName) {
        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(tag.getComponent(), DnDConstants.ACTION_COPY_OR_MOVE, new DragGestureListener() {
            public void dragGestureRecognized(DragGestureEvent dge) {
                dge.startDrag(null, new Transferable() {
                    public DataFlavor[] getTransferDataFlavors() {
                        return new DataFlavor[] {tagDataVlavor};
                    }

                    public boolean isDataFlavorSupported(DataFlavor flavor) {
                        return flavor.equals(tagDataVlavor);
                    }

                    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                        return transferDataProvider.getTransferData(tag, flavor);
                    }
                });
            }
        });
    }
}
