package org.yield4j.tests.ifstatement;
import static org.yield4j.YieldSupport.*;

//>> [0]
public class SingleYieldReturnInIf {
    @org.yield4j.Generator public Iterable<Integer> method() {
		if (true)
			yield_return(0);
	}
}
