package org.yield4j.tests.mixed;
import static org.yield4j.YieldSupport.*;

//>> [0, 1, 4, 9, 8]
public class YieldReturnInIfInFor {
    @org.yield4j.Generator public Iterable<Integer> method() {
		for (int i = 0; i < 5; i++) {
			if (i % 2 == 0)
				yield_return(i * 2);
			else if (i % 3 == 0)
				yield_return(i * 3);
			else
				yield_return(i);
		}
	}
}
