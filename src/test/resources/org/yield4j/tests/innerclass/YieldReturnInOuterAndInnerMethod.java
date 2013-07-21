package org.yield4j.tests.innerclass;

import static org.yield4j.YieldSupport.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

//>> [0, 6]
public class YieldReturnInOuterAndInnerMethod {

    private int x;

    public Iterable<Integer> method() {
        List<Integer> list = new ArrayList<Integer>();
        for (int i : helper()) {
            list.add(i);
        }
        list.add(x);
        return list;
    }
    
    @org.yield4j.Generator private Iterable<Integer> helper() {
        yield_return(0);
        for (Integer i : new Iterable<Integer>() {

            public Iterator<Integer> iterator() {
                return actual().iterator();
            }

            @org.yield4j.Generator private Iterable<Integer> actual() {
                yield_return(1);
                yield_return(2);
                yield_return(3);
            }
        }) {
            x += i;
        }
    }
}
