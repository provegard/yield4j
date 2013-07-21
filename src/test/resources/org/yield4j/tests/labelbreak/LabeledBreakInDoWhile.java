package org.yield4j.tests.labelbreak;
import static org.yield4j.YieldSupport.*;

//>> [outer:0, inner:0]
public class LabeledBreakInDoWhile {
    @org.yield4j.Generator public Iterable<String> method() {
		int i = 0;
		OUTER: do {
			yield_return("outer:" + i++);
			int j = 0;
			do {
				yield_return("inner:" + j++);
				break OUTER;
			} while (j < 3);
		} while (i < 3);
	}
}
