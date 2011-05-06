package ru.swing.html.configuration;

import ru.swing.html.DomModel;

import java.io.IOException;
import java.io.InputStream;

/**
 * Service for loading resources. Different tag can contain links to the resources, such as images
 * etc. The implementations must load resources according to provided url.
 */
public interface ResourceLoader {

    /**
     * Invoked when a resource is requested.
     * @param model dom model instance
     * @param url url of the resource
     * @return inputstream for the resource or null, if resource is unavailable
     * @throws IOException if en error occured while getting inputstream
     */
    public InputStream loadResource(DomModel model, String url) throws IOException;

}
