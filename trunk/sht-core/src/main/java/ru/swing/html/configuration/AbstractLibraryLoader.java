package ru.swing.html.configuration;

import ru.swing.html.LibraryRegistry;

/**
 * Abstract library loader, contains library registry instance.
 */
public abstract class AbstractLibraryLoader implements LibraryLoader {

    private LibraryRegistry libraryRegistry = new LibraryRegistry();


    public LibraryRegistry getLibraryRegistry() {
        return libraryRegistry;
    }
}
