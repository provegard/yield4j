package org.yield4j.tests.nonlocal;
import static org.yield4j.YieldSupport.*;

//>> [5]
public class CallToOtherMethodInSameClass {
    @org.yield4j.Generator public Iterable<Integer> method() {
        yield_return(five());
    }
    
    private int five() {
        return 5;
    }
}
