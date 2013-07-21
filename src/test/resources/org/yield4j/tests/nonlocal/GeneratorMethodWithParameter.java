package org.yield4j.tests.nonlocal;
import static org.yield4j.YieldSupport.*;

//>> [5]
public class GeneratorMethodWithParameter {

	public Iterable<Integer> method() {
		return realone(5);
	}
	
	@org.yield4j.Generator private Iterable<Integer> realone(int n) {
		yield_return(n);
	}
}
