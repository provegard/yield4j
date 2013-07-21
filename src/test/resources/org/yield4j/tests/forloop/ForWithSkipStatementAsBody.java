package org.yield4j.tests.forloop;

import static org.yield4j.YieldSupport.yield_return;

//>> [5]
public class ForWithSkipStatementAsBody {
    @org.yield4j.Generator public Iterable<Integer> method() {
        int i = 0;
        for (; i < 5; i++);
        yield_return(i);
    }
}
