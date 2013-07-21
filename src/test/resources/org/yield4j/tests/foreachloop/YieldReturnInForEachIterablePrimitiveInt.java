package org.yield4j.tests.foreachloop;
import static org.yield4j.YieldSupport.*;

import java.util.Arrays;
import java.util.List;

//>> [3, 4, 5]
public class YieldReturnInForEachIterablePrimitiveInt {
    @org.yield4j.Generator public Iterable<Integer> method() {
		List<Integer> nums = Arrays.asList(3, 4, 5);
		for (int n : nums) {
			yield_return(n);
		}
	}
}
