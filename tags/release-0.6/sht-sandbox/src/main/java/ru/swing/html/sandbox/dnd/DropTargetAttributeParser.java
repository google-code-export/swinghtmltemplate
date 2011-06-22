package ru.swing.html.sandbox.dnd;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.configuration.AttributeParser;
import ru.swing.html.configuration.MethodInvoker;
import ru.swing.html.tags.Tag;
import ru.swing.html.tags.swing.ScrollPane;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 17.05.11
 * Time: 13:00
 * To change this template use File | Settings | File Templates.
 */
public class DropTargetAttributeParser implements AttributeParser {


    private Log logger = LogFactory.getLog(getClass());

    public void applyAttribute(final Tag tag, final JComponent component, String attrName) {

        final String acceptedMime = tag.getAttribute("accept-mime");

        final String ondrop = tag.getAttribute("ondrop");
        if (StringUtils.isEmpty(ondrop)) {
            logger.warn(tag+" 'ondrop' attribute must be specified");
            return;
        }
        MethodInvoker invoker = tag.getModel().getConfiguration().getMethodResolverService().resolveMethod(ondrop, tag);
        final DropDelegator delegator = new DropDelegator(invoker);

        DropTarget dropTarget = new DropTarget(component, new DropTargetListener() {
            public void dragEnter(DropTargetDragEvent dtde) {

                if (StringUtils.isEmpty(acceptedMime)) {
                    return;
                }

                List<DataFlavor> flavors = dtde.getCurrentDataFlavorsAsList();
                List<String> mimes = new ArrayList<String>();
                for (DataFlavor f : flavors) {
                    mimes.add(f.getPrimaryType()+"/"+f.getSubType());
                }
                if (!mimes.contains(acceptedMime)) {
                    logger.info(mimes+" does not contain "+acceptedMime);
                    dtde.rejectDrag();
                }
            }

            public void dragOver(DropTargetDragEvent dtde) {
                if (tag instanceof DropTargetListener) {
                    ((DropTargetListener)tag).dragOver(dtde);
                }
            }

            public void dropActionChanged(DropTargetDragEvent dtde) {
                if (tag instanceof DropTargetListener) {
                    ((DropTargetListener)tag).dropActionChanged(dtde);
                }
            }

            public void dragExit(DropTargetEvent dte) {
            }

            public void drop(DropTargetDropEvent dtde) {
                Object res = delegator.drop(dtde);
                if (res!=null && Boolean.class.equals(res.getClass())) {
                    dtde.dropComplete((Boolean) res);
                }
                else {
                    logger.warn(tag+ ": method "+ondrop+" should return boolean value, rejecting drop.");
                    dtde.dropComplete(false);
                }
            }
        });
        dropTarget.setActive(true);
    }

}
