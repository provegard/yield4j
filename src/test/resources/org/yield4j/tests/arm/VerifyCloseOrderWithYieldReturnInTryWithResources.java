package org.yield4j.tests.arm;
import static org.yield4j.YieldSupport.*;

import java.util.*;

//@@ 1.[789]
//>> [0, 26]
public class VerifyCloseOrderWithYieldReturnInTryWithResources {
    private int i = 3;
    
    public Iterable<Integer> method() {
        List<Integer> list = new ArrayList<>();
        for (int x : gen()) {
            list.add(x);
        }
        list.add(i);
        return list;
    }
    
    // src - src2 = (((3 + 1) * 2) + 1) * 3 = 27
    // src2 - src = (((3 + 1) * 3) + 1) * 2 = 26 
    @org.yield4j.Generator private Iterable<Integer> gen() {
        try (IntegerSource src = new IntegerSource(2); IntegerSource src2 = new IntegerSource(3)) {
            yield_return(0);
        }
    }
    
    private class IntegerSource implements AutoCloseable {
        private final int mul;
        
        private IntegerSource(int mul) {
            this.mul = mul;
        }
        
        public void close() {
            i = (i + 1) * mul;
        }
    }
}
