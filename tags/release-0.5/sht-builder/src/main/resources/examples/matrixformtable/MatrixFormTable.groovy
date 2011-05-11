package ru.swing.html.example;


import java.awt.HeadlessException
import java.awt.List
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import javax.swing.JFrame
import org.jdesktop.observablecollections.ObservableCollections
import org.jdom.JDOMException
import ru.swing.html.Binder
import ru.swing.html.DomModel
import ru.swing.html.ModelElement

/**
 * Build matrix and bind it to formTable
 */
public class FormTableArray extends JFrame {


    @ModelElement("formModel")
    private Model formModel;
    
    private DomModel model;
    private Integer w = 35;
    private Integer h = 35;

    public FormTableArray() throws HeadlessException, JDOMException, IOException {

        formModel = new Model(w, h);
    }

    public void dump() {
        Random random = new Random();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                formModel.datas.get(i).get(j).setValue(random.nextInt(50));
            }
        }

    }


    public Model getFormModel() {
        return formModel;
    }

    public void setFormModel(Model formModel) {
        this.formModel = formModel;
    }

    public void setModel(DomModel model) {
    	  this.model = model;
    }

    public DomModel getModel() {
    	  return model;
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

        public java.util.List<java.util.List<Data>> getDatas() {
            return datas;
        }

        public void setDatas(java.util.List<java.util.List<Data>> datas) {
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
