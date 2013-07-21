package org.yield4j.tests.trycatchfinally;
import static org.yield4j.YieldSupport.*;

import java.util.*;

//>> [0, 1, 5]
public class ThrowExceptionWithFinally {
    private int value;
    
    public Iterable<Integer> method() {
        List<Integer> result = new ArrayList<Integer>();
        try {
            for (int x : gen()) {
                result.add(x);
            }
        } catch (Exception e) {
            result.add(value);
        }
        return result;
    }
    
    @org.yield4j.Generator private Iterable<Integer> gen() {
        try {
            yield_return(0);
            yield_return(1);
            throw new RuntimeException();
        } finally {
            value = 5;
        }
    }
}
