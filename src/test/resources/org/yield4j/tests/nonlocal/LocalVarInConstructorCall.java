package org.yield4j.tests.nonlocal;

import java.util.Arrays;

//>> [55]
public class LocalVarInConstructorCall {
    @org.yield4j.Generator public Iterable<Integer> method() throws InterruptedException {
        int i = 55;
        Foo foo = new Foo(i);
        yield_return(foo.x);
    }
    
    private class Foo {
        int x;
        Foo(int x) {
            this.x = x;
        }
    }
}
