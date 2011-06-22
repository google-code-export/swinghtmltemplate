package ru.swing.html.tags.event;

import junit.framework.TestCase;
import ru.swing.html.Utils;
import ru.swing.html.configuration.MethodInvoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseDelegatorTest extends TestCase {

    public void testDelegateNoParams() throws Exception {
        final Foo foo = new Foo();
        assertEquals(0, foo.i);

        final Method m = Utils.findActionMethod(Foo.class, "foo", Integer.class);
        CustomDelegator del = new CustomDelegator(new MethodInvoker() {
            public Object invoke(Class argType, Object arg) {
                try {
                    return m.invoke(foo);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        del.doTest(null);
        assertEquals(1, foo.i);
    }

    public void testDelegate1Param() throws Exception {
        final Foo1 foo = new Foo1();
        assertEquals(0, foo.i);

        final Method m = Utils.findActionMethod(Foo1.class, "foo", Integer.class);
        CustomDelegator del = new CustomDelegator(new MethodInvoker() {
            public Object invoke(Class argType, Object arg) {
                try {
                    return m.invoke(foo, arg);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        del.doTest(10);
        assertEquals(10, foo.i);
    }



    public class CustomDelegator extends BaseDelegator {

        public CustomDelegator(MethodInvoker invoker) {
            super(invoker);
        }

        public void doTest(Object event) {
            delegate(Object.class, event);
        }
    }

    public class Foo {

        public int i = 0;

        public void foo() {
            i++;
        }
    }

    public class Foo1 {

        public int i = 0;

        public void foo(Integer i) {
            this.i = i;
        }
    }
}
