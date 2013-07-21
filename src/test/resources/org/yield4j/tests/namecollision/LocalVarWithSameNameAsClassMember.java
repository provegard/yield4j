package org.yield4j.tests.namecollision;
import static org.yield4j.YieldSupport.*;

//>> [7]
public class LocalVarWithSameNameAsClassMember {
    private int x = 5;
    
    @org.yield4j.Generator public Iterable<Integer> method() {
        int x = 2;
        yield_return(x + this.x);
    }
}
