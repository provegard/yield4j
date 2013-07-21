package org.yield4j.error;
import static org.yield4j.YieldSupport.*;

//!! ERROR(8) unreachable code
public class UnreachableDoWhileStatement {
    @org.yield4j.Generator public Iterable<Integer> method() {
        yield_break();
        do {
            yield_return(1);
        } while (false);
    }
}
