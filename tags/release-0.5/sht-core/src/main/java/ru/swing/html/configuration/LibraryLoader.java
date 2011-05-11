package ru.swing.html.configuration;

import ru.swing.html.LibraryRegistry;

/**
 * Base interface for tag factories registry.
 */
public interface LibraryLoader {

    /**
     * <p>
     * This method is called in DomLoader when before loading document.
     * </p>
     * <p>
     *     Accessors must load all needed TagFactories and store them in the library registry.
     * </p>
     */
    public void loadLibraries();

    /**
     * Must return a library registry with all loaded TagFactories
     * @return library registry
     */
    public LibraryRegistry getLibraryRegistry();

}
