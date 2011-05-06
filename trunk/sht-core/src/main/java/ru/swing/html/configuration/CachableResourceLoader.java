package ru.swing.html.configuration;

import ru.swing.html.DomModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * This resource loader caches all requested resources, so mupltiple request for the same resource
 * will use cache.
 */
public abstract class CachableResourceLoader implements ResourceLoader {

    private Map<String, InputStream> cachedStreams = new HashMap<String, InputStream>();

    public InputStream loadResource(DomModel model, String url) throws IOException {
        //check cache
        if (cachedStreams.containsKey(url)) {
            return cachedStreams.get(url);
        }
        //if resource is not chached, load it
        InputStream in = fetchResourceAsStream(model, url);
        //cache resource
        cachedStreams.put(url, in);
        return in;
    }

    /**
     * Implementations must load resource in this method.
     * @param model dom model instance
     * @param url url of the resource
     * @return inputstream of the resource
     * @throws IOException if an io error occured
     */
    public abstract InputStream fetchResourceAsStream(DomModel model, String url) throws IOException;
}
