package ru.swing.html.example.wizard;

import org.jdom.JDOMException;

import java.io.IOException;

public class CreatePersonWizard extends WizardPane {


    public CreatePersonWizard() {
        super("/ru/swing/html/example/wizard/default.html");
        addPage(new Page("index", "Wellcome", "/ru/swing/html/example/wizard/indexpage.html"));
        addPage(new PersonPage("/ru/swing/html/example/wizard/personpage.html"));
        addPage(new LastPageNotification("last", "Congratulations", "/ru/swing/html/example/wizard/lastpage.html"));
    }


    @Override
    public void onWizardFinish() {
        System.out.println(getModel());
        System.exit(0);
    }

    public static void main(String[] args) throws JDOMException, IOException {
        Person model = new Person();
        new CreatePersonWizard().execute(model);
    }



}
