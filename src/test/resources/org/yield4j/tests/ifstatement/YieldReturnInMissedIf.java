package org.yield4j.tests.ifstatement;
import static org.yield4j.YieldSupport.*;

//>> [0]
public class YieldReturnInMissedIf {
    @org.yield4j.Generator public Iterable<Integer> method() {
		int i = 0;
		if (i > 0)
			yield_return(1);
		yield_return(i);
	}
}
