package ru.swing.html.configuration;

/**
 * Default implementation for configuration.
 */
public class DefaultConfiguration implements Configuration {

    private LibraryLoader libraryLoader = new ServiceLibraryLoader();
    private ResourceLoader resourceLoader = new ClasspathResourceLoader();

    public LibraryLoader getLibraryLoader() {
        return libraryLoader;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}
