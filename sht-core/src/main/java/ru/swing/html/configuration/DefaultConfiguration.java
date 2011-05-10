package ru.swing.html.configuration;

/**
 * Default implementation for configuration.
 */
public class DefaultConfiguration implements Configuration {

    private LibraryLoader libraryLoader = new ServiceLibraryLoader();
    private ResourceLoader resourceLoader = new ClasspathResourceLoader();
    private AttributeParser attributeParser = new DefaultAttributeParser();

    public LibraryLoader getLibraryLoader() {
        return libraryLoader;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public AttributeParser getAttributeParser() {
        return attributeParser;
    }
}
