package org.yield4j.tests.foreachloop;
import static org.yield4j.YieldSupport.*;

//>> [3, 4, 5]
public class YieldReturnInForEachArrayPrimitiveLong {
    @org.yield4j.Generator public Iterable<Long> method() {
		long[] nums = new long[] {3l, 4l, 5l};
		for (long n : nums) {
			yield_return(n);
		}
	}
}
