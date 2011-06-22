package ru.swing.html.sandbox.dnd;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdom.JDOMException;
import ru.swing.html.Binder;
import ru.swing.html.DomLoader;
import ru.swing.html.DomModel;
import ru.swing.html.ModelElement;
import ru.swing.html.configuration.DefaultAttributeParser;
import ru.swing.html.tags.*;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.Object;
import java.util.*;
import java.util.List;

public class DndForm extends JFrame {

    private Log logger = LogFactory.getLog(getClass());

    @ModelElement("source")
    private java.util.List source;

    @ModelElement("target")
    private java.util.List target;

    private DomModel model;


    @ModelElement("model")
    private Model formModel = new Model();


    public DndForm() throws JDOMException, IOException {

        source = ObservableCollections.observableList(new ArrayList<Object>());
        target = ObservableCollections.observableList(new ArrayList<Object>());

        source.add("Item 1");
        source.add("Item 2");
        source.add("Item 3");
        source.add("Item 4");

        model = DomLoader.loadModel(getClass().getResourceAsStream("/ru/swing/html/sandbox/dnd/DndForm.html"));
        DefaultAttributeParser parser = (DefaultAttributeParser) model.getConfiguration().getAttributeParser();
        parser.setParserForAttribute("dragsource", new DragSourceAttributeParser());
        parser.setParserForAttribute("droptarget", new DropTargetAttributeParser());
        Binder.bind(this, true, model);
        
    }



    public void onDblclick(MouseEvent e) {
        Tag sourceTag = model.query((JComponent)e.getSource()).get(0);
        Collection source;
        Collection target;
        Collection selection;
        if (sourceTag.getId().equals("source")) {
            source = this.source;
            target = this.target;
            selection = formModel.selectedSource;
        }
        else {
            source = this.target;
            target = this.source;
            selection = formModel.selectedTarget;
        }
        target.addAll(selection);
        source.removeAll(selection);

    }


    public boolean onDrop(DropTargetDropEvent e) {
        DropTarget source1 = (DropTarget) e.getSource();

        Tag sourceTag = model.query((JComponent) source1.getComponent()).get(0);
        Collection source;
        Collection target;
        if (sourceTag.getId().equals("source")) {
            source = this.target;
            target = this.source;
        }
        else {
            source = this.source;
            target = this.target;
        }

        Object transferData = null;
        try {
            transferData = e.getTransferable().getTransferData(DragSourceAttributeParser.tagDataVlavor);
        } catch (UnsupportedFlavorException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }


        if (transferData instanceof Object[]) {
            Object[] data = (Object[]) transferData;
            Set set = new TreeSet();
            set.addAll(target);
            set.addAll(Arrays.asList(data));
            target.clear();
            target.addAll(set);
            source.removeAll(Arrays.asList(data));
            logger.info("Dropped array: "+ data[0]);
        }
        else {
            logger.info("Dropped: "+ transferData);
        }
        return false;
    }



    public static void main(String[] args) throws JDOMException, IOException {
        final DndForm form = new DndForm();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                form.setVisible(true);
            }
        });
    }


    public class Model {

        private java.util.List selectedSource = ObservableCollections.observableList(new ArrayList<Object>());

        private java.util.List selectedTarget = ObservableCollections.observableList(new ArrayList<Object>());

        public List getSelectedSource() {
            return selectedSource;
        }

        public void setSelectedSource(List selectedSource) {
            this.selectedSource = selectedSource;
        }

        public List getSelectedTarget() {
            return selectedTarget;
        }

        public void setSelectedTarget(List selectedTarget) {
            this.selectedTarget = selectedTarget;
        }
    }

}
