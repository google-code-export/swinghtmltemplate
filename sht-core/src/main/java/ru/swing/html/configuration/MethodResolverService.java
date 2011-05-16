package ru.swing.html.configuration;

import ru.swing.html.tags.Tag;

/**
 * Resolves methods against  dom model elements and invokes them.
 */
public interface MethodResolverService {

    public MethodInvoker resolveMethod(String methodString, Tag tag);

}
