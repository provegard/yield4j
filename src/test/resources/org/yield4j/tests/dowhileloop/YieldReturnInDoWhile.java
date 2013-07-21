package org.yield4j.tests.dowhileloop;
import static org.yield4j.YieldSupport.*;

//>> [0, 1, 2, 3, 4]
public class YieldReturnInDoWhile {
    @org.yield4j.Generator public Iterable<Integer> method() {
		int i = 0;
		do {
			yield_return(i++);
		} while (i < 5);
	}
}
