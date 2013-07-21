package org.yield4j.tests.foreachloop;
import static org.yield4j.YieldSupport.*;

//>> [3, 4, 5]
public class YieldReturnInForEachArrayPrimitiveShort {
    @org.yield4j.Generator public Iterable<Short> method() {
		short[] nums = new short[] {(short) 3, (short) 4, (short) 5};
		for (short n : nums) {
			yield_return(n);
		}
	}
}
