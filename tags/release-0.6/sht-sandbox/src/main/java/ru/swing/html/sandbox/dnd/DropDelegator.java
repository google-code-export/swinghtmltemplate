package ru.swing.html.sandbox.dnd;

import ru.swing.html.configuration.MethodInvoker;
import ru.swing.html.tags.event.BaseDelegator;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

public class DropDelegator extends BaseDelegator {

    public DropDelegator(MethodInvoker invoker) {
        super(invoker);
    }

    public void dragEnter(DropTargetDragEvent dtde) {
        delegate(DropTargetDragEvent.class, dtde);
    }

    public void dragOver(DropTargetDragEvent dtde) {
        delegate(DropTargetDragEvent.class, dtde);
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
        delegate(DropTargetDragEvent.class, dtde);
    }

    public void dragExit(DropTargetEvent dte) {
        delegate(DropTargetEvent.class, dte);
    }

    public Object drop(DropTargetDropEvent dtde) {
        return delegate(DropTargetDropEvent.class, dtde);
    }
}
