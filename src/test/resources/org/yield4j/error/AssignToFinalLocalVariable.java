package org.yield4j.error;
import static org.yield4j.YieldSupport.*;

//!! ERROR(9) cannot assign a value to final variable y
public class AssignToFinalLocalVariable {
    @org.yield4j.Generator public Iterable<Integer> method() {
        int x = 5;
        final int y = x;
        y = 7;
        yield_return(y);
    }
}
