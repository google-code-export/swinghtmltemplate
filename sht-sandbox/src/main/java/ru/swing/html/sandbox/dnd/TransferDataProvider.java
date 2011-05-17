package ru.swing.html.sandbox.dnd;

import ru.swing.html.tags.Tag;

import java.awt.datatransfer.DataFlavor;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 17.05.11
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 */
public interface TransferDataProvider {

    public Object getTransferData(Tag tag, DataFlavor flavor);
}
