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


    public void onPageShow(DomModel domModel, Object model) {
    }


    /**
     * Invoked before swinching to the  next page.
     * @param domModel dom model
     * @param model wizard model object
     * @return false of switching is forbidden
     */
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
