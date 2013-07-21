package org.yield4j.warning;
import static org.yield4j.YieldSupport.yield_return;

//!! WARNING(10) Yield in try with finalizer is dangerous; there is no guarantee that the finalizer will be executed.
public class WarnOnYieldInTryWithinTryWithFinally {
    @org.yield4j.Generator public Iterable<Integer> method() {
        int i = 0;
        try {
            try {
                yield_return(i);
            } catch (Exception e) {
            }
        } finally {
            i++;
        }
        yield_return(i);
    }
}
