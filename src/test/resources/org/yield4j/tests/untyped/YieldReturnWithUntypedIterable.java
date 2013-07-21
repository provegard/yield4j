package org.yield4j.tests.untyped;
import static org.yield4j.YieldSupport.*;

//>> [1, a]
public class YieldReturnWithUntypedIterable {
    @org.yield4j.Generator public Iterable method() {
        yield_return(1);
        yield_return("a");
    }
}
