package org.yield4j.tests.whileloop;
import static org.yield4j.YieldSupport.*;

//>> [0, 1, 2, 3, 4]
public class YieldReturnInWhile {
    @org.yield4j.Generator public Iterable<Integer> method() {
		int i = 0;
		while (i < 5) {
			yield_return(i++);
		}
	}
}
