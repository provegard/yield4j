package org.yield4j.tests.forloop;
import static org.yield4j.YieldSupport.*;

//>> [1, 2, 3, 4, 5]
public class ForWithYieldReturnAsUpdate {

    @org.yield4j.Generator public Iterable<Integer> method() {
        for (int i = 0; i < 5; i++, yield_return(i)) {
        }
    }
}
