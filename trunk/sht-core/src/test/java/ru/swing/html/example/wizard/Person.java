package ru.swing.html.example.wizard;

import java.beans.PropertyChangeSupport;

public class Person {

    private String name;
    private String lastName;
    private String phone;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);


    public String getName() {
        return name;
    }

    public void setName(String name) {
        String old = this.name;
        this.name = name;
        pcs.firePropertyChange("name", old, name);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        String old = this.lastName;
        this.lastName = lastName;
        pcs.firePropertyChange("lastName", old, lastName);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        String old = this.phone;
        this.phone = phone;
        pcs.firePropertyChange("phone", old, phone);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
