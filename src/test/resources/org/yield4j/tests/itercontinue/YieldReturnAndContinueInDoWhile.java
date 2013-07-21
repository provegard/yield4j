package org.yield4j.tests.itercontinue;
import static org.yield4j.YieldSupport.*;

//>> [1, 2, 2, 3, 6, 4, 8, 5, 10]
public class YieldReturnAndContinueInDoWhile {
    @org.yield4j.Generator public Iterable<Integer> method() {
		int i = 0;
		do {
			i++;
			yield_return(i);
			if (i == 2)
				continue;
			yield_return(i * 2);
		} while (i < 5);
	}
}
