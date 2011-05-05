package ru.swing.html.example.nbnewproject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;
import org.jdom.JDOMException;
import ru.swing.html.Binder;
import ru.swing.html.DomModel;
import ru.swing.html.ModelElement;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Netbeans new project
 * http://i175.photobucket.com/albums/w126/hairulazami/simple_swing_loginform_and_getText/java_app00008_500.jpg
 */
public class NewProjectForm extends JPanel {

    private Log logger = LogFactory.getLog(getClass());
    private DomModel domModel;

    @ModelElement("projectsTreeModel")
    private TreeModel projectsTreeModel;
    private ProjectsService projectsService;

    @ModelElement("model")
    private Model model = new Model();

    @ModelElement("projectTypeRenderer")
    private ProjectTypeRenderer projectTypeRenderer = new ProjectTypeRenderer();



    public NewProjectForm() {

        projectsService = new ProjectsService();
        projectsTreeModel = projectsService.createTreeModel();


        //enable/disable "Next" button
        model.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (domModel==null) {
                    return;

                }
                Tag finishBtnTag = domModel.getTagById("nextBtn");
                if (finishBtnTag==null || finishBtnTag.getComponent()==null) {
                    return;
                }
                if (evt.getPropertyName().equals("selectedProject") && evt.getNewValue()!=null
                        && !(evt.getNewValue().equals(model.empty))) {
                    finishBtnTag.getComponent().setEnabled(true);
                }
                else {
                    finishBtnTag.getComponent().setEnabled(false);
                }
            }
        });

        try {
            domModel = Binder.bind(this, true);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onCategoryChange(TreeSelectionEvent e) {
        if (e.getNewLeadSelectionPath()!=null) {
            TreePath path = e.getNewLeadSelectionPath();
            if (path.getPathCount()>1) {
                //remove root
                Object[] tokens = path.getPath();
                String[] withoutRoot = new String[tokens.length-1];
                for (int i = 0; i<withoutRoot.length; i++) {
                    withoutRoot[i] = String.valueOf(tokens[i+1]);
                }
                java.util.List<ProjectType> projects = projectsService.getProjectsByPath(withoutRoot);
                model.setProjects(projects);

                model.setSelectedProject(null);
            }
        }
    }


    public void onNextClick() {
        logger.info("Selected project type: "+model.getSelectedProject());
    }


    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public static void main(String[] args) {
        NewProjectForm form = new NewProjectForm();

        final JFrame f = new JFrame("New Project");
        f.setSize(800, 700);

        f.getContentPane().add(form);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                f.setVisible(true);
            }
        });
    }


    public class Model {


        public final ProjectType empty = new ProjectType("", "", "", null);

        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
        private java.util.List<ProjectType> projects = ObservableCollections.observableList(new ArrayList<ProjectType>());
        private ProjectType selectedProject;

        public List<ProjectType> getProjects() {
            return projects;
        }

        public void setProjects(List<ProjectType> projects) {
            Object old = this.projects;
            this.projects.clear();
            this.projects.addAll(projects);
            pcs.firePropertyChange("projects", old, projects);
        }

        public ProjectType getSelectedProject() {
            return selectedProject;
        }

        public void setSelectedProject(ProjectType selectedProject) {
            Object old = this.selectedProject;
            this.selectedProject = selectedProject == null ? empty : selectedProject;
            pcs.firePropertyChange("selectedProject", old, this.selectedProject);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }
    }

}
