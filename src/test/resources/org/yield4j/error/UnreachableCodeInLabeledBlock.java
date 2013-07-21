package org.yield4j.error;
import static org.yield4j.YieldSupport.*;

//!! ERROR(9) unreachable code
public class UnreachableCodeInLabeledBlock {
    @org.yield4j.Generator public Iterable<Integer> method() {
        foo: {
            yield_break();
            yield_return(1);
        }
    }
}
