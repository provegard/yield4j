package org.yield4j.error;
import static org.yield4j.YieldSupport.*;

//!! ERROR(8) unreachable code
public class UnreachableTryStatement {
    @org.yield4j.Generator public Iterable<Integer> method() {
        yield_break();
        try {
            System.out.println(1);
        } finally {
        }
    }
}
