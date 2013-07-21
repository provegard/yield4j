package org.yield4j.tests.innerclass;
import static org.yield4j.YieldSupport.*;

//>> [5]
public class YieldReturnInMethodInInnerClass {

    public Iterable<Integer> method() {
        return new Inner().method();
    }
    
    private class Inner {
        @org.yield4j.Generator Iterable<Integer> method() {
            yield_return(5);
        }
    }
}
