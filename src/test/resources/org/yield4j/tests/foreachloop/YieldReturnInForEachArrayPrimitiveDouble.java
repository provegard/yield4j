package org.yield4j.tests.foreachloop;
import static org.yield4j.YieldSupport.*;

//>> [3.0, 4.0, 5.0]
public class YieldReturnInForEachArrayPrimitiveDouble {
    @org.yield4j.Generator public Iterable<Double> method() {
		double[] nums = new double[] {3.0, 4.0, 5.0};
		for (double n : nums) {
			yield_return(n);
		}
	}
}
