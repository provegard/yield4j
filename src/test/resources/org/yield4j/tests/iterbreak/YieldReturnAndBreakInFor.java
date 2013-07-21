package org.yield4j.tests.iterbreak;
import static org.yield4j.YieldSupport.*;

//>> [0, 0, 1, 2, 2]
public class YieldReturnAndBreakInFor {
    @org.yield4j.Generator public Iterable<Integer> method() {
		for (int i = 0; i < 5; i++) {
			yield_return(i);
			if (i == 2)
				break;
			yield_return(i * 2);
		}
	}
}
