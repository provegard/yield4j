package org.yield4j.tests.namecollision;

import static org.yield4j.YieldSupport.yield_return;

//@@ 1.[789]
//>> [true, true, true, true]
public class TryWithResourcesNameConflict {
    
    @org.yield4j.Generator public Iterable<Boolean> method() {
        Throwable primaryExc0 = new Exception("a");
        Throwable suppressedExc0 = new Exception("b");
        Throwable t0 = new Exception("c");
        try (IntegerSource src = new IntegerSource()) {
            for (boolean b : new boolean[] {true}) {
                yield_return(b);
            }
        }
        yield_return(primaryExc0.getMessage().equals("a"));
        yield_return(suppressedExc0.getMessage().equals("b"));
        yield_return(t0.getMessage().equals("c"));
    }
    
    private class IntegerSource implements AutoCloseable {
        public void close() {
        }
    }
}
