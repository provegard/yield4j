package org.yield4j.error;
import static org.yield4j.YieldSupport.*;

//!! ERROR(7) yield_break takes no arguments
public class YieldBreakWithArgs {
    @org.yield4j.Generator public Iterable<String> method() {
		yield_break(1);
	}
}
