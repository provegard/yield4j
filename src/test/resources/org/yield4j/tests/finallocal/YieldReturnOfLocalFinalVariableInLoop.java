package org.yield4j.tests.finallocal;
import static org.yield4j.YieldSupport.*;

//>> [7, 7, 7]
public class YieldReturnOfLocalFinalVariableInLoop {
    @org.yield4j.Generator public Iterable<Integer> method() {
        final int x = 7;
        for (int i = 0; i < 3; i++) {
            yield_return(x);
        }
    }
    
}
