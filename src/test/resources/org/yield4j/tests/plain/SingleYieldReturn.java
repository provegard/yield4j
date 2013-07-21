package org.yield4j.tests.plain;
import static org.yield4j.YieldSupport.*;

//>> [1]
public class SingleYieldReturn {
    @org.yield4j.Generator public Iterable<Integer> method() {
		yield_return(1);
	}
}
