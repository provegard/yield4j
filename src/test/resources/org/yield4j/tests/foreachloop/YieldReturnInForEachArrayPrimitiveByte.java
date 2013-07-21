package org.yield4j.tests.foreachloop;
import static org.yield4j.YieldSupport.*;

//>> [3, 4, 5]
public class YieldReturnInForEachArrayPrimitiveByte {
    @org.yield4j.Generator public Iterable<Byte> method() {
		byte[] nums = new byte[] {(byte) 3, (byte) 4, (byte) 5};
		for (byte n : nums) {
			yield_return(n);
		}
	}
}
