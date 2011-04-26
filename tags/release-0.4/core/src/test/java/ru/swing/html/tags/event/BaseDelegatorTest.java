package ru.swing.html.tags.event;

import junit.framework.TestCase;
import ru.swing.html.Utils;

import java.lang.reflect.Method;

public class BaseDelegatorTest extends TestCase {

    public void testDelegateNoParams() throws Exception {
        Foo foo = new Foo();
        assertEquals(0, foo.i);

        Method m = Utils.findActionMethod(Foo.class, "foo", Integer.class);
        CustomDelegator del = new CustomDelegator(foo, m);
        del.doTest(null);
        assertEquals(1, foo.i);
    }

    public void testDelegate1Param() throws Exception {
        Foo1 foo = new Foo1();
        assertEquals(0, foo.i);

        Method m = Utils.findActionMethod(Foo1.class, "foo", Integer.class);
        CustomDelegator del = new CustomDelegator(foo, m);
        del.doTest(10);
        assertEquals(10, foo.i);
    }



    public class CustomDelegator extends BaseDelegator {

        public CustomDelegator(Object controller, Method finalM) {
            super(controller, finalM);
        }

        public void doTest(Object event) {
            delegate(event);
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
