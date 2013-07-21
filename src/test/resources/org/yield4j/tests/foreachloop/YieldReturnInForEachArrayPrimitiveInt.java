package org.yield4j.tests.foreachloop;
import static org.yield4j.YieldSupport.*;

//>> [3, 4, 5]
public class YieldReturnInForEachArrayPrimitiveInt {
    @org.yield4j.Generator public Iterable<Integer> method() {
		int[] nums = new int[] {3, 4, 5};
		for (int n : nums) {
			yield_return(n);
		}
	}
}
