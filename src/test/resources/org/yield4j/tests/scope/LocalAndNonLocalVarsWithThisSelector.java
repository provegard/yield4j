package org.yield4j.tests.scope;
import static org.yield4j.YieldSupport.yield_return;

//>> [0, 10, 1, 10, 2, 10]
public class LocalAndNonLocalVarsWithThisSelector {
    private int i = 10;
    @org.yield4j.Generator public Iterable<Integer> method() {
        for (int i = 0; i < 3; i++) {
            yield_return(i);
            yield_return(this.i);
        }
    }
}
