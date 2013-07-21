package org.yield4j.tests.trycatchfinally;
import static org.yield4j.YieldSupport.*;

//>> [0, 1]
public class YieldReturnInTryWithFinally {
    @org.yield4j.Generator public Iterable<Integer> method() {
        int i = 0;
        try {
            yield_return(i);
        } finally {
            i++;
        }
        yield_return(i);
    }
}
