package ru.swing.html.example.wizard;

import ru.swing.html.DomModel;

/**
 * Disables "back" and "cancel" buttons on page show.
 */
public class LastPageNotification extends Page {
    public LastPageNotification(String id, String title, String pageUrl) {
        super(id, title, pageUrl);
    }


    @Override
    public void onPageShow(DomModel domModel, Object model) {
        domModel.query("#pageBack").enabled("false");
        domModel.query("#cancel").enabled("false");
    }
}
