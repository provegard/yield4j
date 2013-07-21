package org.yield4j.error;
import static org.yield4j.YieldSupport.*;

//!! ERROR(8) unreachable code
public class UnreachableForStatement {
    @org.yield4j.Generator public Iterable<Integer> method() {
        yield_break();
        for (
                int i = 0; i < 5; i++) {
            yield_return(i);
        }
    }
}
