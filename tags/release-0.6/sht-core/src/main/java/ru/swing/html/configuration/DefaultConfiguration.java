package ru.swing.html.configuration;

/**
 * Default implementation for configuration.
 */
public class DefaultConfiguration implements Configuration {

    private LibraryLoader libraryLoader = new ServiceLibraryLoader();
    private ResourceLoader resourceLoader = new ClasspathResourceLoader();
    private AttributeParser attributeParser = new DefaultAttributeParser();
    private LayoutService layoutService = new DefaultLayoutService();
    private MethodResolverService methodResolverService = new DefaultMethodResolverService();

    public LibraryLoader getLibraryLoader() {
        return libraryLoader;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public AttributeParser getAttributeParser() {
        return attributeParser;
    }

    public LayoutService getLayoutService() {
        return layoutService;
    }

    public MethodResolverService getMethodResolverService() {
        return methodResolverService;
    }
}
