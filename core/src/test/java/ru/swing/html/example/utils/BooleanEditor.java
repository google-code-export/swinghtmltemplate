package ru.swing.html.example.utils;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Deady
 * Date: 13.04.11
 * Time: 17:51
 */
public class BooleanEditor extends DefaultCellEditor  {

    public BooleanEditor() {
        super(new JCheckBox());
        JCheckBox checkBox = (JCheckBox)getComponent();
        checkBox.setHorizontalAlignment(JCheckBox.CENTER);
    }

}