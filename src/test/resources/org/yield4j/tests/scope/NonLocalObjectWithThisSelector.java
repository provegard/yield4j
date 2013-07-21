package org.yield4j.tests.scope;
import static org.yield4j.YieldSupport.yield_return;

//>> [4, 5]
public class NonLocalObjectWithThisSelector {
    private I i = new I();
    @org.yield4j.Generator public Iterable<Integer> method() {
        int i = 4;
        yield_return(i);
        yield_return(this.i.x);
    }
    
    private class I {
        int x = 5;        
    }
}
