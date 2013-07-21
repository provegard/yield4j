package org.yield4j.tests.trycatchfinally;
import static org.yield4j.YieldSupport.*;

//>> [0, 2]
public class YieldReturnWithNestedFinallies {
    @org.yield4j.Generator public Iterable<Integer> method() {
        int i = 0;
        try {
            try {
                yield_return(i);
            } finally {
                i++;
            }
        } finally {
            i++;
        }
        yield_return(i);
    }
}
