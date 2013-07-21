package org.yield4j.tests.nonlocal;
import static org.yield4j.YieldSupport.*;

//>> [5]
public class UseOfClassFieldViaThis {
    private int x = 5;
    
    @org.yield4j.Generator public Iterable<Integer> method() {
        yield_return(this.x);
    }
}
