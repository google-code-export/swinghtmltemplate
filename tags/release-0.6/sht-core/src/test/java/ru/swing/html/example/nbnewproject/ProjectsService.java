package ru.swing.html.example.nbnewproject;

import org.apache.commons.lang.StringUtils;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 15.04.11
 * Time: 11:07
 */
public class ProjectsService {


    private List<ProjectType> projects;
    private Map<String[], List<ProjectType>> projectsByPath;

    public ProjectsService() {

        projects = new ArrayList<ProjectType>();

        projects.add(new ProjectType("Java Application", "/img/java_icon.png", "<b>Creates a new Java SE application</b> in a standart IDE project", new String[] {"Java"}));
        projects.add(new ProjectType("Java Desktop Application", "/img/java_icon.png", "<b>Creates a new Java SE Desktop application</b> in a standart IDE project", new String[] {"Java"}));
        projects.add(new ProjectType("Java Class Library", "/img/java_icon.png", "<b>Creates a new Java SE library</b> in a standart IDE project", new String[] {"Java"}));
        projects.add(new ProjectType("JavaFX Application", "/img/java_icon.png", "<b>Creates a new Java FX application</b> in a standart IDE project", new String[] {"JavaFX"}));
        projects.add(new ProjectType("JavaFX Application Sample", "/img/java_icon.png", "<b>Creates a new Java FX application sample</b> in a standart IDE project", new String[] {"Samples", "Java"}));


        projectsByPath = new HashMap<String[], List<ProjectType>>();
        for (ProjectType pt : projects) {
            String[] path = pt.getPath();
            List<ProjectType> projects;
            if (projectsByPath.containsKey(path)) {
                projects = projectsByPath.get(path);
            }
            else {
                projects = new ArrayList<ProjectType>();
                projectsByPath.put(path, projects);
            }
            projects.add(pt);
        }

    }


    public List<ProjectType> getProjectsByPath(String[] path) {
        TreePath param = new TreePath(path);
        List<ProjectType> res = new ArrayList<ProjectType>();
        for (String[] key : projectsByPath.keySet()) {
            TreePath pathKey = new TreePath(key);
            if (pathKey.equals(param)) {
                res.addAll(projectsByPath.get(key));
            }

        }
        return res;
    }


    public TreeModel createTreeModel() {


        Map<TreePath, DefaultMutableTreeNode> nodesByPath = new TreeMap<TreePath, DefaultMutableTreeNode>(new Comparator<TreePath>() {
            public int compare(TreePath o1, TreePath o2) {
                String path1 = StringUtils.join(o1.getPath());
                String path2 = StringUtils.join(o2.getPath());
                return path1.compareTo(path2);
            }
        });

        for (String[] path : projectsByPath.keySet()) {
            for (int i = 1; i<=path.length; i++) {

                String[] subPath = new String[i];
                System.arraycopy(path, 0, subPath, 0, subPath.length);
                TreePath treePath = new TreePath(subPath);

                if (!nodesByPath.containsKey(treePath)) {
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(subPath[subPath.length-1]);
                    nodesByPath.put(treePath, node);
                }

            }
        }


        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Projects");

        for (TreePath path : nodesByPath.keySet()) {
            DefaultMutableTreeNode node = nodesByPath.get(path);
            if (path.getPathCount()>1) {
                TreePath parentPath = path.getParentPath();
                DefaultMutableTreeNode parentNode = nodesByPath.get(parentPath);
                parentNode.add(node);
            }
            else {
                rootNode.add(node);
            }
        }

        TreeModel model = new DefaultTreeModel(rootNode);
        return model;
    }

}
