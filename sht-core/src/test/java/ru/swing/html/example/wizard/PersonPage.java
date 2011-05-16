package ru.swing.html.example.wizard;

import org.apache.commons.lang.StringUtils;
import ru.swing.html.DomModel;

import javax.swing.*;

public class PersonPage extends Page {


    public PersonPage(String pageUrl) {
        super("person", "Enter details", pageUrl);
    }


    @Override
    public boolean beforeNextPage(DomModel domModel, Object model) {
        Person person = (Person) model;

        boolean res = true;

        if (StringUtils.isBlank(person.getName())) {
            domModel.query("label[for='name']").addClass("error");
            res = false;
        }
        else {
            domModel.query("label[for='name']").removeClass("error");
        }

        if (StringUtils.isBlank(person.getLastName())) {
            domModel.query("label[for='lastname']").addClass("error");
            res = false;
        }
        else {
            domModel.query("label[for='lastname']").removeClass("error");
        }

        if (StringUtils.isBlank(person.getPhone())) {
            domModel.query("label[for='phone']").addClass("error");
            res = false;
        }
        else {
            domModel.query("label[for='phone']").removeClass("error");
        }
        return res;
    }
}
