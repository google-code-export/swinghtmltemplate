package ru.swing.html;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds tag factories for different namespaces
 */
public class LibraryRegistry {

    public static final String EMPTY_NAMESPACE = ".empty";
    private Map<String, TagFactory> factories = new HashMap<String, TagFactory>();

    public void registerLibrary(String namespace, TagFactory factory) {
        factories.put(StringUtils.isNotEmpty(namespace) ? namespace : EMPTY_NAMESPACE, factory);
    }

    public TagFactory getTagFactory(String namespace) {
        return factories.get(StringUtils.isNotEmpty(namespace) ? namespace : EMPTY_NAMESPACE);
    }
}
