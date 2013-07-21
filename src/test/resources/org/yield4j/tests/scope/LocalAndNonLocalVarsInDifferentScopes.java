package org.yield4j.tests.scope;
import static org.yield4j.YieldSupport.*;

//>> [0, 1, 2, 3, 10]
public class LocalAndNonLocalVarsInDifferentScopes {
    private int i = 10;
    @org.yield4j.Generator public Iterable<Integer> method() {
        for (int i = 0; i < 4; i++) {
            yield_return(i);
        }
        yield_return(i);
    }
}
