package ru.swing.html.builder;

import org.apache.commons.lang.StringUtils;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 26.04.11
 * Time: 13:54
 */
public class ExamplesService {

    private List<Example> examples;
    private Map<String[], List<Example>> examplesByPath;

    public ExamplesService() {

        examples = new ArrayList<Example>();

        examples.add(new Example("'Create project' form", "examples/createproject/CreateProjectForm.html", new String[] {"Forms"}));
        examples.add(new Example("Login form", "examples/loginform/LoginForm.html", new String[] {"Forms"}));


        examplesByPath = new HashMap<String[], List<Example>>();
        for (Example pt : examples) {
            String[] path = pt.getPath();
            List<Example> exampleList;
            if (examplesByPath.containsKey(path)) {
                exampleList = examplesByPath.get(path);
            }
            else {
                exampleList = new ArrayList<Example>();
                examplesByPath.put(path, exampleList);
            }
            exampleList.add(pt);
        }


    }


    public List<Example> getExamplesByPath(String[] path) {
        TreePath param = new TreePath(path);
        List<Example> res = new ArrayList<Example>();
        for (String[] key : examplesByPath.keySet()) {
            TreePath pathKey = new TreePath(key);
            if (pathKey.equals(param)) {
                res.addAll(examplesByPath.get(key));
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

        for (String[] path : examplesByPath.keySet()) {
            for (int i = 1; i<=path.length; i++) {

                String[] subPath = new String[i];
                System.arraycopy(path, 0, subPath, 0, subPath.length);
                TreePath treePath = new TreePath(subPath);

                if (!nodesByPath.containsKey(treePath)) {
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(subPath[subPath.length-1], true);
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

        for (String[] path : examplesByPath.keySet()) {
            TreePath treePath = new TreePath(path);
            DefaultMutableTreeNode node = nodesByPath.get(treePath);
            List<Example> examples = examplesByPath.get(path);
            for (Example example : examples) {
                DefaultMutableTreeNode exampleNode = new DefaultMutableTreeNode(example);
                node.add(exampleNode);
            }
        }

        TreeModel model = new DefaultTreeModel(rootNode);
        return model;
    }


}
