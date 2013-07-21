package org.yield4j.tests.foreachloop;
import static org.yield4j.YieldSupport.*;

//>> [3.0, 4.0, 5.0]
public class YieldReturnInForEachArrayPrimitiveFloat {
    @org.yield4j.Generator public Iterable<Float> method() {
		float[] nums = new float[] {3.0f, 4.0f, 5.0f};
		for (float n : nums) {
			yield_return(n);
		}
	}
}
