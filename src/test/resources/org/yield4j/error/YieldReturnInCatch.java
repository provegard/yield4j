package org.yield4j.error;
import static org.yield4j.YieldSupport.*;

//!! ERROR(10) yield_return cannot appear in a catch block
public class YieldReturnInCatch {
    @org.yield4j.Generator public Iterable<Integer> method() {
        try {
            throw new NullPointerException();
        } catch (Exception e) {
            yield_return(0);
        }
    }
}
