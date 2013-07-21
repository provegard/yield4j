package org.yield4j.tests.namecollision;
import static org.yield4j.YieldSupport.*;

//>> [1]
public class InnerClassNameOccupied {
    @org.yield4j.Generator public Iterable<Integer> method() {
        yield_return(1);
    }

    private class $__GeneratorIterator_0
    {
    }
}
