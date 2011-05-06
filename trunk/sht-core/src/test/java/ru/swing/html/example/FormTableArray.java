package ru.swing.html.example;

import org.jdesktop.observablecollections.ObservableCollections;
import org.jdom.JDOMException;
import ru.swing.html.Binder;
import ru.swing.html.DomModel;
import ru.swing.html.ModelElement;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 06.05.11
 * Time: 14:36
 */
public class FormTableArray extends JFrame {


    @ModelElement("model")
    private Model model;
    private DomModel domModel;
    private Integer w = 55;
    private Integer h = 55;

    public FormTableArray() throws HeadlessException, JDOMException, IOException {

        model = new Model(w, h);
        domModel = Binder.bind(this, true);
    }

    public void dump() {
        Random random = new Random();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                model.datas.get(i).get(j).setValue(random.nextInt(50));
            }
        }

    }


    public static void main(String[] args) throws JDOMException, IOException {
        new FormTableArray().setVisible(true);
    }


    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public class Model {
        
        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
        private java.util.List<java.util.List<Data>> datas;


        public Model(int width, int height) {

            datas = ObservableCollections.observableList(new ArrayList<List<Data>>(height));


            for (int i = 0; i < width; i++) {
                datas.add(ObservableCollections.observableList(new ArrayList<Data>(width)));
                for (int j = 0; j < height; j++) {
                    datas.get(i).add(new Data());
                    datas.get(i).get(j).setValue(10);
                }
            }
        }

        public List<List<Data>> getDatas() {
            return datas;
        }

        public void setDatas(List<List<Data>> datas) {
            this.datas = datas;
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(propertyName, listener);
        }
    }


    public class Data {

        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
        private int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            int old = this.value;
            this.value = value;
            pcs.firePropertyChange("value", old, value);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }
    }
}
