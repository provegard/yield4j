package org.yield4j.tests.labelbreak;
import static org.yield4j.YieldSupport.*;

//>> [outer:0, inner:0]
public class LabeledBreakInWhile {
    @org.yield4j.Generator public Iterable<String> method() {
		int i = 0;
		OUTER: while (i < 3) {
			yield_return("outer:" + i++);
			int j = 0;
			while (j < 3) {
				yield_return("inner:" + j++);
				break OUTER;
			}
		}
	}
}
