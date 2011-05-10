package ru.swing.html.configuration;

/**
 * <p>
 *     Configuration holds different services, that can be used while loading and converting dom model.
 * </p>
 * <p>
 *     Configuration is individual for each dom model.
 * </p>
 *
 */
public interface Configuration {

    /**
     * Returns service for loading tag factories. All tag factories can be accessed
     * by namespace from libraryRegistry, contained in library loader
     * @return library loader
     */
    public LibraryLoader getLibraryLoader();

    /**
     * Returns service for loading resources by their url.
     * @return resource loader
     */
    public ResourceLoader getResourceLoader();

    /**
     * Returns the service for the parsing and applying common attribute values to the component.
     * @return atribute parser
     */
    public AttributeParser getAttributeParser();
}
