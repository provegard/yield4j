package org.yield4j.tests.trycatchfinally;
import static org.yield4j.YieldSupport.*;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

//>> [0, 5]
public class ThrowCheckedExceptionInGenerator {
    
    public Iterable<Integer> method() {
        List<Integer> result = new ArrayList<Integer>();
        try {
            for (int x : gen()) {
                result.add(x);
            }
        } catch (IOException e) {
            result.add(5);
        }
        return result;
    }
    
    @org.yield4j.Generator private Iterable<Integer> gen() throws IOException {
        yield_return(0);
        throw new IOException();
    }
}
