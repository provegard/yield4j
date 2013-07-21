package org.yield4j.tests.exception;
import static org.yield4j.YieldSupport.*;

//>> [1]
public class ExceptionPropagation {
    @org.yield4j.Generator public Iterable<Integer> method() throws Throwable {
        yield_return(value());
    }
    
    private int value() throws Throwable {
        return 1;
    }
}
