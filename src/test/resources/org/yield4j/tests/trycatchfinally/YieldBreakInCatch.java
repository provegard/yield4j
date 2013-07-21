package org.yield4j.tests.trycatchfinally;
import static org.yield4j.YieldSupport.*;

import java.util.ArrayList;
import java.util.List;

//>> [1]
public class YieldBreakInCatch {
    @org.yield4j.Generator public Iterable<Integer> method() {
        try {
            yield_return(1);
            throw new Exception();
        } catch (Exception e) {
            yield_break();
        }
        yield_return(2);
    }
}
