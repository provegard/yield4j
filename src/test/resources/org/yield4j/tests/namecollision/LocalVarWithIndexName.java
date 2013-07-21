package org.yield4j.tests.namecollision;
import static org.yield4j.YieldSupport.*;

//>> [5, 6]
public class LocalVarWithIndexName {
    @org.yield4j.Generator public Iterable<Integer> method() {
        int index = 5;
        yield_return(index);
        yield_return(index + 1);
    }
}
