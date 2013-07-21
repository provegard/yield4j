package org.yield4j.error;
import static org.yield4j.YieldSupport.*;

//!! ERROR(8) unreachable code
public class UnreachableIfStatement {
    @org.yield4j.Generator public Iterable<Integer> method() {
        yield_break();
        if (true)
            yield_return(1);
    }
}
