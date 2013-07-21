package org.yield4j.tests.itercontinue;
import static org.yield4j.YieldSupport.*;

//>> [0, 0, 1, 2, 2, 3, 6, 4, 8]
public class YieldReturnAndContinueInFor {
    @org.yield4j.Generator public Iterable<Integer> method() {
		for (int i = 0; i < 5; i++) {
			yield_return(i);
			if (i == 2)
				continue;
			yield_return(i * 2);
		}
	}
}
