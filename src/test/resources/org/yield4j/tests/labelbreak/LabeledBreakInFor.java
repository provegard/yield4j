package org.yield4j.tests.labelbreak;
import static org.yield4j.YieldSupport.*;

//>> [outer:0, inner:0]
public class LabeledBreakInFor {
    @org.yield4j.Generator public Iterable<String> method() {
		OUTER: for (int i = 0; i < 3; i++) {
			yield_return("outer:" + i);
			for (int j = 0; j < 3; j++) {
				yield_return("inner:" + j);
				break OUTER;
			}
		}
	}
}
