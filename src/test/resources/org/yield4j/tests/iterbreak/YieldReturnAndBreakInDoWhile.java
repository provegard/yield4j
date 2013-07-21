package org.yield4j.tests.iterbreak;
import static org.yield4j.YieldSupport.*;

//>> [0, 0, 1, 2, 2]
public class YieldReturnAndBreakInDoWhile {
    @org.yield4j.Generator public Iterable<Integer> method() {
		int i = 0;
		do {
			yield_return(i);
			if (i == 2)
				break;
			yield_return(i * 2);
			i++;
		} while (i < 5);
	}
}
