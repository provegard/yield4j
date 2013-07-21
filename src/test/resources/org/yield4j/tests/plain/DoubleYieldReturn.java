package org.yield4j.tests.plain;
import static org.yield4j.YieldSupport.*;

//>> [a, b]
public class DoubleYieldReturn {
    @org.yield4j.Generator public Iterable<String> method() {
		yield_return("a");
		yield_return("b");
	}
}
