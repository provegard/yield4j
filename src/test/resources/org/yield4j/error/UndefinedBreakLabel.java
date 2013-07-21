package org.yield4j.error;
import static org.yield4j.YieldSupport.*;

//!! ERROR(9) undefined label: OUTER
public class UndefinedBreakLabel {
    @org.yield4j.Generator public Iterable<Integer> method() {
		for (int j = 0; j < 3; j++) {
			yield_return(j);
			break OUTER;
		}
	}
}
