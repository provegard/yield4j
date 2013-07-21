package org.yield4j.tests.nonlocal;
import static org.yield4j.YieldSupport.*;

//>> [5]
public class CallToIteratorMethod {
    @org.yield4j.Generator public Iterable<Integer> method() {
        yield_return(next());
    }
    
    private int next() {
        return 5;
    }
}
