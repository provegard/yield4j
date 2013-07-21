package org.yield4j.warning;

import static org.yield4j.YieldSupport.yield_return;

//!! WARNING(10) Yield in try with finalizer is dangerous; there is no guarantee that the finalizer will be executed.
public class WarnOnYieldInTryWithFinally {
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
