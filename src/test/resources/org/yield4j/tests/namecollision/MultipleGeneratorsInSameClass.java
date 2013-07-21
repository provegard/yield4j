package org.yield4j.tests.namecollision;
import static org.yield4j.YieldSupport.*;

public class MultipleGeneratorsInSameClass {
    //>> [1]
    @org.yield4j.Generator public Iterable<Integer> method1() {
        yield_return(1);
    }

    //>> [2]
    @org.yield4j.Generator public Iterable<Integer> method2() {
        yield_return(2);
    }
}
