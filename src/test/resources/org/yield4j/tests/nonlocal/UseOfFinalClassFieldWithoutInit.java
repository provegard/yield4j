package org.yield4j.tests.nonlocal;
import static org.yield4j.YieldSupport.*;

//>> [5]
public class UseOfFinalClassFieldWithoutInit {

    final int x;
    
    public UseOfFinalClassFieldWithoutInit() {
        x = 5;
    }
    
    @org.yield4j.Generator public Iterable<Integer> method() {
        yield_return(x);
    }
}
