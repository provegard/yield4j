package org.yield4j.error;
import static org.yield4j.YieldSupport.*;

//!! ERROR(8) unreachable code
public class UnreachableWhileStatement {
    @org.yield4j.Generator public Iterable<Integer> method() {
        yield_break();
        while (false) {
            yield_return(1);
        }
    }
}
