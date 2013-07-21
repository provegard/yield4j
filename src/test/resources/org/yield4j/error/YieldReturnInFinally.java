package org.yield4j.error;
import static org.yield4j.YieldSupport.*;

//!! ERROR(11) yield_return cannot appear in a finally block
public class YieldReturnInFinally {
    @org.yield4j.Generator public Iterable<Integer> method() {
        int i;
        try {
            i = 0;
        } finally {
            yield_return(i);
        }
    }
}
