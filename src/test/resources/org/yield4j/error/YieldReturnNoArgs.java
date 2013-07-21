package org.yield4j.error;
import static org.yield4j.YieldSupport.*;

//!! ERROR(7) yield_return takes exactly one argument
public class YieldReturnNoArgs {
    @org.yield4j.Generator public Iterable<Integer> method() {
		yield_return();
	}
}
