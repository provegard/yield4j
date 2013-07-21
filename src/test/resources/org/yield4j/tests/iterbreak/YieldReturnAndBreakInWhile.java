package org.yield4j.tests.iterbreak;
import static org.yield4j.YieldSupport.*;

//>> [0, 0, 1, 2, 2]
public class YieldReturnAndBreakInWhile {
    @org.yield4j.Generator public Iterable<Integer> method() {
		int i = 0;
		while (i < 5) {
			yield_return(i);
			if (i == 2)
				break;
			yield_return(i * 2);
			i++;
		}
	}
}
