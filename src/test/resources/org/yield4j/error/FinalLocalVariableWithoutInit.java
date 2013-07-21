package org.yield4j.error;

import static org.yield4j.YieldSupport.yield_return;

//!! ERROR(9) The final local variable y must be initialized at declaration time.
public class FinalLocalVariableWithoutInit {
    @org.yield4j.Generator public Iterable<Integer> method() {
        int x = 5;
        final int y;
        y = x;
        yield_return(y);
    }
}
