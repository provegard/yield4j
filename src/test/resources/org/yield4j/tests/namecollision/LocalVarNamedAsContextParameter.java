package org.yield4j.tests.namecollision;
import static org.yield4j.YieldSupport.*;

//>> [5]
public class LocalVarNamedAsContextParameter {
    @org.yield4j.Generator public Iterable<Integer> method() {
        int $context = 5;
        yield_return($context);
    }
}
