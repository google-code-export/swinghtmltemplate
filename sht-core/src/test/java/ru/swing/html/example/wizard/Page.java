package ru.swing.html.example.wizard;

import ru.swing.html.DomModel;

public class Page {

    private String pageUrl;
    private String id;
    private String title;

    public Page(String id, String title, String pageUrl) {
        this.pageUrl = pageUrl;
        this.id = id;
        this.title = title;
    }


    public void onPageShow() {
    }


    public boolean beforeNextPage(DomModel domModel, Object model) {
        return true;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

}
