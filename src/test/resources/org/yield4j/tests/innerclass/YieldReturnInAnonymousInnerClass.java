package org.yield4j.tests.innerclass;
import static org.yield4j.YieldSupport.*;
import java.util.Iterator;

//>> [1, 2, 3]
public class YieldReturnInAnonymousInnerClass {

    @org.yield4j.Generator public Iterable<Integer> method() {
        return new Iterable<Integer>() {
            
            public Iterator<Integer> iterator() {
                return actual().iterator();
            }
            
            @org.yield4j.Generator private Iterable<Integer> actual() {
                yield_return(1);
                yield_return(2);
                yield_return(3);
            }
        };
    }
}
