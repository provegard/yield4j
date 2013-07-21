package org.yield4j.tests.arm;
import static org.yield4j.YieldSupport.*;

//@@ 1.[789]
//>> [1, 2, 3]
public class YieldReturnInTryWithResources {

    @org.yield4j.Generator public Iterable<Integer> method() {
        try (IntegerSource src = new IntegerSource()) {
            for (int i : src.numbers()) {
                yield_return(i);
            }
        }
    }
    
    private class IntegerSource implements AutoCloseable {
        
        public int[] numbers() {
            return new int[] {1, 2, 3};
        }
        
        public void close() {
        }
    }
}
