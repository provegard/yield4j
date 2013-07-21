package org.yield4j.tests.itercontinue;
import static org.yield4j.YieldSupport.*;

import java.util.List;
import static java.util.Arrays.asList;

//>> [0, 0, 1, 2, 2, 3, 6, 4, 8]
public class YieldReturnAndContinueInForEach {
    @org.yield4j.Generator public Iterable<Integer> method() {
		List<Integer> nums = asList(0, 1, 2, 3, 4);
		for (int i : nums) {
			yield_return(i);
			if (i == 2)
				continue;
			yield_return(i * 2);
		}
	}
}
