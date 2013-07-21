package org.yield4j.tests.plain;
import static org.yield4j.YieldSupport.*;

//>> [0, 1]
public class YieldReturnWithLocalVariable {
    @org.yield4j.Generator public Iterable<Integer> method() {
		int i = 0;
		yield_return(i++);
		yield_return(i);
	}
}
