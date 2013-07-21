package org.yield4j.tests.trycatchfinally;
import static org.yield4j.YieldSupport.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//>> [0]
public class YieldReturnWithThrowAndFinally {
    public Iterable<Integer> method() {
        List<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iterator = gen().iterator();
        while (true) {
            try {
                if (iterator.hasNext()) {
                    result.add(iterator.next());
                } else {
                    break;
                }
            } catch (Exception ignore) {
            }
        }
        return result;
    }
    
    @org.yield4j.Generator private Iterable<Integer> gen() {
        int i = 0;
        try {
            yield_return(i);
            throw new NullPointerException();
        } finally {
            i++;
        }
        yield_return(i);
    }
}
