package org.yield4j.error;
import static org.yield4j.YieldSupport.*;

//!! ERROR(9) unreachable code
public class UnreachableInitLessForStatement {
    @org.yield4j.Generator public Iterable<Integer> method() {
        int i = 0;
        yield_break();
        for (; i < 5; i++) {
            yield_return(i);
        }
    }
}
