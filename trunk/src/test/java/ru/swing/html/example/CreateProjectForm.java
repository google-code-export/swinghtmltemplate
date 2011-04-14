package ru.swing.html.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;
import ru.swing.html.Binder;
import ru.swing.html.DomModel;
import ru.swing.html.ModelElement;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * http://captcha.biz/images/help/java/screenshot007_t.jpg
 */
public class CreateProjectForm extends JPanel {

    private Log logger = LogFactory.getLog(getClass());
    private DomModel domModel;

    @ModelElement("runtimeValues")
    private List<String> runtimeValues = Arrays.asList("Apache Tomcat v5.5", "Apache Tomcat v6.0");

    @ModelElement("versionValues")
    private List<String> versionValues = Arrays.asList("2.3", "2.4", "2.5");


    @ModelElement("model")
    private Model model = new Model();

    public CreateProjectForm() {

        model.setName("captchaWeb");
        model.setUseDefaultPath(true);
        model.setRuntime("Apache Tomcat v6.0");
        model.setVersion("2.5");
        model.setConfiguration("Apache Tomcat v6.0");

        try {
            domModel = Binder.bind(this, true);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        onDefaultPathClick();

    }



    public void onDefaultPathClick() {
        JCheckBox box = (JCheckBox) domModel.getTagById("default").getComponent();
        for (Tag tag : domModel.query(".pathElement")) {
            tag.getComponent().setEnabled(!box.isSelected());
        }
    }

    public void onFinish() {
        logger.info("Got model: "+model);
    }


    public static void main(String[] args) {
        CreateProjectForm form = new CreateProjectForm();

        JFrame f = new JFrame("New Dynamic Web Project");
        f.setSize(800, 700);

        f.getContentPane().add(form);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }


    public class Model {

        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
        private String name;
        private boolean useDefaultPath;
        private String path;
        private String runtime;
        private String version;
        private String configuration;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isUseDefaultPath() {
            return useDefaultPath;
        }

        public void setUseDefaultPath(boolean useDefaultPath) {
            this.useDefaultPath = useDefaultPath;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getRuntime() {
            return runtime;
        }

        public void setRuntime(String runtime) {
            this.runtime = runtime;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getConfiguration() {
            return configuration;
        }

        public void setConfiguration(String configuration) {
            this.configuration = configuration;
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }

        @Override
        public String toString() {
            return "Model{" +
                    "name='" + name + '\'' +
                    ", useDefaultPath=" + useDefaultPath +
                    ", path='" + path + '\'' +
                    ", runtime='" + runtime + '\'' +
                    ", version='" + version + '\'' +
                    ", configuration='" + configuration + '\'' +
                    '}';
        }
    }

}
