package ru.swing.html.configuration;

import org.apache.commons.lang.StringUtils;
import ru.swing.html.DomModel;

import java.io.InputStream;

/**
 * <p>This resource loader loads resources from classpath, available to classloader.
 * <p>The url may start with "/", in this case the url is treated as absolute, the first symbol will be ignored.
 * <p>If the url doesn't start with "/", then it is treated as relative. The base path is taken from dom model
 * source path. If model is null or model.sourcePath is null, then empty path is used as base path.
 *
 *
 * @see DomModel@getSourcePath
 */
public class ClasspathResourceLoader implements ResourceLoader{

    public InputStream loadResource(DomModel model, String url) {


        //if absolute path
        if (url.startsWith("/")) {
            //getClassLoader().getResourceAsStream понимает пути без ведущего /, например,
            //"ru/swing/html/example/loginform.css"
            url = url.substring(1);
        }
        //if relative path - substitute path from dom model path
        else if (model!=null && StringUtils.isNotEmpty(model.getSourcePath())) {
            String parentPath = model.getSourcePath();
            int indexOfFilename = parentPath.lastIndexOf('/');
            if (indexOfFilename>=0) {
                parentPath = parentPath.substring(0, indexOfFilename);
            }
            if (parentPath.startsWith("/")) {
                parentPath = parentPath.substring(1);
            }

            url = parentPath + "/" + url;
        }

        ClassLoader cl = getClass().getClassLoader();
        return cl.getResourceAsStream(url);
    }
}
