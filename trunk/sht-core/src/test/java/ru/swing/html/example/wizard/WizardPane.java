package ru.swing.html.example.wizard;

import org.jdom.JDOMException;
import ru.swing.html.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WizardPane extends JDialog {

    @ModelElement("pages")
    private List<Page> pages = new ArrayList<Page>();

    private String templateUrl;

    private Page activePage;

    @Bind("pages")
    private JPanel pagesContainer;

    @ModelElement("model")
    private Object model;

    private DomModel domModel;

    public WizardPane(String templateUrl) {
        this.templateUrl = templateUrl;
    }

    public void addPage(Page page) {
        pages.add(page);
    }


    public void execute(Object model) throws IOException, JDOMException {
        this.model = model;
        domModel = DomLoader.loadModel(getClass().getResourceAsStream(templateUrl));
        domModel.setSourcePath("ru/swing/html/example/wizard/");
        Binder.bind(this, true, domModel);
        setActivePage(pages.get(0));
        setVisible(true);
    }


    public final void onBack() {
        int index;
        if (getActivePage()!=null) {
            index = pages.indexOf(getActivePage());
            index--;
            if (index<0) {
                return;
            }
        }
        else {
            index = 0;
        }

        domModel.query("#pageBack").enabled(index>0 ? "true" : "false");
        domModel.query("#pageNext").enabled(index<pages.size()-1 ? "true" : "false");
        domModel.query("#finish").enabled(index>=pages.size()-1 ? "true" : "false");

        setActivePage(pages.get(index));
        getActivePage().onPageShow(domModel, getModel());
        CardLayout layout = (CardLayout) pagesContainer.getLayout();
        layout.show(pagesContainer, getActivePage().getId());
    }


    public final void onNext() {
        int index;
        if (getActivePage()!=null) {
            if (!getActivePage().beforeNextPage(domModel, getModel())) {
                return;
            }
            index = pages.indexOf(getActivePage());
            if (index>=pages.size()-1) {
                return;
            }
            index++;
        }
        else {
            index = 0;
        }
        domModel.query("#pageBack").enabled(index>0 ? "true" : "false");
        domModel.query("#pageNext").enabled(index<pages.size()-1 ? "true" : "false");
        domModel.query("#finish").enabled(index>=pages.size()-1 ? "true" : "false");

        setActivePage(pages.get(index));
        getActivePage().onPageShow(domModel, model);
        CardLayout layout = (CardLayout) pagesContainer.getLayout();
        layout.show(pagesContainer, getActivePage().getId());

    }


    public final void onFinish() {
        if (!getActivePage().beforeNextPage(domModel, getModel())) {
            return;
        }
        onWizardFinish();
        dispose();
    }

    public void onWizardFinish() {
    }

    public final void onCancel() {
        dispose();
    }

    public List<Page> getPages() {
        return pages;
    }

    public Page getActivePage() {
        return activePage;
    }

    public void setActivePage(Page activePage) {
        this.activePage = activePage;
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }
}
