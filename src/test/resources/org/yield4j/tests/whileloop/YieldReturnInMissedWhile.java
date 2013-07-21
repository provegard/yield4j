package org.yield4j.tests.whileloop;
import static org.yield4j.YieldSupport.*;

//>> []
public class YieldReturnInMissedWhile {
    @org.yield4j.Generator public Iterable<Integer> method() {
		int i = 0;
		while (i != 0) {
			yield_return(i++);
		}
	}
}
