package org.yield4j.tests.foreachloop;
import static org.yield4j.YieldSupport.*;

//>> [true, false]
public class YieldReturnInForEachArrayPrimitiveBoolean {
    @org.yield4j.Generator public Iterable<Boolean> method() {
		boolean[] bools = new boolean[] {true, false};
		for (boolean b : bools) {
			yield_return(b);
		}
	}
}
