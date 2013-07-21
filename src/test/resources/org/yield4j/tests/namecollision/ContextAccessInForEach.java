package org.yield4j.tests.namecollision;
import static org.yield4j.YieldSupport.*;

//>> [6]
public class ContextAccessInForEach {
    private int $context = 1;
    
    @org.yield4j.Generator public Iterable<Integer> method() {
        for (int i : new Integer[] { 5 }) {
            yield_return($context + i);
        }
    }
}
