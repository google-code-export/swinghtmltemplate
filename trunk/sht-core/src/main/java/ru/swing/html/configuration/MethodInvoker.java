package ru.swing.html.configuration;

public interface MethodInvoker {

    /**
     * Invokes method with the specified args. If args is null or args.length==0 then method
     * to invoke is method with no args.
     * @param args arguments to method
     */
    public void invoke(Class argType, Object arg);

}
