package org.yield4j.tests.forloop;
import static org.yield4j.YieldSupport.*;

//>> [0, 0, 1, 2, 2, 4, 3, 6, 4, 8]
public class MultipleYieldReturnInFor {
    @org.yield4j.Generator public Iterable<Integer> method() {
		for (int i = 0; i < 5; i++) {
			yield_return(i);
			yield_return(i * 2);
		}
	}
}
