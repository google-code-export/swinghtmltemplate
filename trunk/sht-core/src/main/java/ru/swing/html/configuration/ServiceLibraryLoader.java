package ru.swing.html.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.swing.html.TagFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * <p>
 * This class loads tag factories from description file.
 * </p>
 * <p>
 *     File must be stored in "META-INF/services/ru.swing.html.TagFactory".
 * </p>
 * <p>
 *     File must be of properties file format, it must contain an list of pairs "key=value" on each
 *     line where
 *     <ul>
 *         <li>key - the full classname of the Factory class</li>
 *         <li>value - The namespace this class is responsible for</li>
 *     </ul>
 *
 *     Factory class must be an instance of TagFactory. It must contain default constructor.
 * </p>
 * @see TagFactory
 */
public class ServiceLibraryLoader extends AbstractLibraryLoader {

    private final Set<ClassLoader> classLoaders = new HashSet<ClassLoader>();
    private final Set<URL> serviceURLs = new HashSet<URL>();
    private Log logger = LogFactory.getLog(getClass());

    public void loadLibraries() {
        loadProvidersIfNecessary();
    }


    private void loadProvidersIfNecessary() {
        ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();

        if (!classLoaders.contains(currentLoader)) {
            classLoaders.add(currentLoader);
            loadProviders(currentLoader);
        }
    }

    private void loadProviders(ClassLoader classLoader) {
        // PENDING: this needs to be rewriten in terms of ServiceLoader
        String serviceName = "META-INF/services/" + TagFactory.class.getName();

        try {
            Enumeration<URL> urls = classLoader.getResources(serviceName);

            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();

                if (!serviceURLs.contains(url)) {
                    serviceURLs.add(url);
                    addProviders(url);
                }
            }
        } catch (IOException ex) {
        }
    }

    private void addProviders(URL url) {

        Properties properties = new Properties();

        logger.trace("Loading tag factories from "+url.getFile());

        try {
            properties.load(url.openStream());
            for (java.lang.Object key : properties.keySet()) {
                String classname = (String) key;
                TagFactory factory;
                try {
                    factory = (TagFactory) Class.forName(classname).newInstance();
                    getLibraryRegistry().registerLibrary(properties.getProperty(classname), factory);
                } catch (InstantiationException e) {
                } catch (IllegalAccessException e) {
                } catch (ClassNotFoundException e) {
                }
            }

        } catch (UnsupportedEncodingException ex) {
        } catch (IOException ex) {
        }

    }



}
