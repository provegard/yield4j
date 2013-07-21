package org.yield4j.tests.arm;
import static org.yield4j.YieldSupport.*;
import java.util.*;

//@@ 1.[789]
//>> [0, 3]
public class VerifyBlockOrderWithYieldReturnInTryWithResources {

    private int i = 3;
    
    public Iterable<Integer> method() {
        List<Integer> list = new ArrayList<>();
        for (int x : gen()) {
            list.add(x);
        }
        list.add(i);
        return list;
    }
    
    // close - catch - finally => ((3 + 1) * 2) - 5 = 3
    // catch - finally - close => ((3 * 2) - 5) + 1 = 2
    @org.yield4j.Generator private Iterable<Integer> gen() {
        try (IntegerSource src = new IntegerSource()) {
            yield_return(0);
            if (true)
                throw new RuntimeException();
        } catch (RuntimeException ex) {
            i *= 2;
        } finally {
            i -= 5;
        }
    }
    
    private class IntegerSource implements AutoCloseable {
        
        public void close() {
            i += 1;
        }
    }
}
