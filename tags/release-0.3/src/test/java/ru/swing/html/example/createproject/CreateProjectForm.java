package ru.swing.html.example.createproject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;
import ru.swing.html.Binder;
import ru.swing.html.DomModel;
import ru.swing.html.ModelElement;
import ru.swing.html.tags.Tag;

import javax.swing.*;
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


}
