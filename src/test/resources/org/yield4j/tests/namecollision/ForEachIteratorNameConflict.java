package org.yield4j.tests.namecollision;
import static org.yield4j.YieldSupport.*;

import static java.util.Arrays.asList;

//>> [5]
public class ForEachIteratorNameConflict {
    private int $it_x = 1;
    
    @org.yield4j.Generator public Iterable<Integer> method() {
        for (int x : asList(4)) {
            yield_return(x + $it_x);
        }
    }
}
