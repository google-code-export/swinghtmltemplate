package examples.createproject

import javax.swing.JCheckBox
import ru.swing.html.DomModel
import ru.swing.html.ModelElement
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import javax.swing.JFileChooser

public class CreateProjectForm {

    private DomModel model;

    @ModelElement("runtimeValues")
    private List<String> runtimeValues = Arrays.asList("Apache Tomcat v5.5", "Apache Tomcat v6.0");

    @ModelElement("versionValues")
    private List<String> versionValues = Arrays.asList("2.3", "2.4", "2.5");

    @ModelElement("model")
    private Model formModel = new Model();

    public CreateProjectForm() {

        formModel.setName("captchaWeb");
        formModel.setUseDefaultPath(true);
        formModel.setRuntime("Apache Tomcat v6.0");
        formModel.setVersion("2.5");
        formModel.setConfiguration("Apache Tomcat v6.0");

    }

    public void setModel(DomModel model) {
        this.model = model;
    }

    public void onDefaultPathClick() {
        JCheckBox box = (JCheckBox) model.getTagById("default").getComponent();

        model.query(".pathElement").each {it.component.enabled = !box.selected;}

    }

    public void onBrowseClick() {
        JFileChooser c = new JFileChooser();
        c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (c.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
            formModel.setPath(c.getSelectedFile().absolutePath);
        }
    }

    public void onFinish() {
        println(formModel.toString());
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
            String old = this.name;
            this.name = name;
            pcs.firePropertyChange("name", old, name);
        }

        public boolean isUseDefaultPath() {
            return useDefaultPath;
        }

        public void setUseDefaultPath(boolean useDefaultPath) {
            boolean old = this.useDefaultPath;
            this.useDefaultPath = useDefaultPath;
            pcs.firePropertyChange("useDefaultPath", old, useDefaultPath);
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            String old = this.path;
            this.path = path;
            pcs.firePropertyChange("path", old, path);
        }

        public String getRuntime() {
            return runtime;
        }

        public void setRuntime(String runtime) {
            String old = this.runtime;
            this.runtime = runtime;
            pcs.firePropertyChange("runtime", old, runtime);
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            String old = this.version;
            this.version = version;
            pcs.firePropertyChange("version", old, version);
        }

        public String getConfiguration() {
            return configuration;
        }

        public void setConfiguration(String configuration) {
            String old = this.configuration;
            this.configuration = configuration;
            pcs.firePropertyChange("configuration", old, configuration);
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
