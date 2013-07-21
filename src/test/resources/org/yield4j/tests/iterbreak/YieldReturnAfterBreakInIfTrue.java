package org.yield4j.tests.iterbreak;
import static org.yield4j.YieldSupport.*;

//>> []
public class YieldReturnAfterBreakInIfTrue {
    @org.yield4j.Generator public Iterable<Integer> method() {
		for (int i = 0; i < 5; i++) {
			if (true)
				break;
			yield_return(i);
		}
	}
}
