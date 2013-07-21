package org.yield4j.tests.trycatchfinally;
import static org.yield4j.YieldSupport.*;

import java.util.ArrayList;
import java.util.List;

//>> [0, 6, 99]
public class ThrowInFinallyTriggeredByException {
    private int value;
    
    public Iterable<Integer> method() {
        List<Integer> result = new ArrayList<Integer>();
        try {
            for (int x : gen()) {
                result.add(x);
            }
        } catch (NullPointerException e) {
            result.add(value); // 6
            result.add(98);
        } catch (IllegalArgumentException e2) {
            result.add(value); // 6
            result.add(99);
        }
        return result;
    }
    
    @org.yield4j.Generator private Iterable<Integer> gen() {
        try
        {
            try {
                yield_return(0);
                throw new NullPointerException();
            } finally {
                value = 5;
                throw new IllegalArgumentException();
            }
        } finally {
            value++;
        }
    }
}
