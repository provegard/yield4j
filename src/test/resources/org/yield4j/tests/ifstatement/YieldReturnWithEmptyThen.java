package org.yield4j.tests.ifstatement;
import static org.yield4j.YieldSupport.*;

//>> [1]
public class YieldReturnWithEmptyThen {

    @org.yield4j.Generator public Iterable<Integer> method() {
        if (5 < 0)
            ;
        else
            yield_return(1);
    }
}
