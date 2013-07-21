package org.yield4j.tests.namecollision;
import static org.yield4j.YieldSupport.*;

//>> [3]
public class LocalVarWithStateName {
    @org.yield4j.Generator public Iterable<Integer> method() {
        String state = "abc";
        yield_return(state.length());
    }
}
