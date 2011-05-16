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
        if (StringUtils.isBlank(person.getName()) || StringUtils.isBlank(person.getLastName()) || StringUtils.isBlank(person.getPhone())) {
            JOptionPane.showMessageDialog(domModel.getWindow(), "All fields must be filled");
            return false;
        }
        return super.beforeNextPage(domModel, model);
    }
}
