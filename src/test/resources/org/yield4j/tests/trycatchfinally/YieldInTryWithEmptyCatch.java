package org.yield4j.tests.trycatchfinally;

import static org.yield4j.YieldSupport.yield_break;

//>> []
public class YieldInTryWithEmptyCatch {
    @org.yield4j.Generator public Iterable<Integer> method() {
        try {
            yield_break();
        } catch (Exception e) {
        }
    }
}
