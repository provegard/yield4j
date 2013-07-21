package org.yield4j.tests.namecollision;
import static org.yield4j.YieldSupport.*;

//>> [5]
public class ClassMemberNamedAsContextParameter {
    private int $context = 5;
    
    @org.yield4j.Generator public Iterable<Integer> method() {
        yield_return($context);
    }
}
