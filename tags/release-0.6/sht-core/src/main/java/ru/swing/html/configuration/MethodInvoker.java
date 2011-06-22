package ru.swing.html.configuration;

public interface MethodInvoker {

    /**
     * Invokes method with the specified args. If args is null or args.length==0 then method
     * to invoke is method with no args.
     * @param argType the type of the possible argument
     * @param arg argument to method
     * @return object, returned from invoked method. null, if method is void.
     * @see java.lang.reflect.Method#invoke(Object, Object...)
     */
    public Object invoke(Class argType, Object arg);

}
