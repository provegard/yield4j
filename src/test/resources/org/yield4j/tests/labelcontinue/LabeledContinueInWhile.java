package org.yield4j.tests.labelcontinue;
import static org.yield4j.YieldSupport.*;

//>> [outer:0, inner:0, inner:1, outer:1, inner:0, inner:1, outer:2, inner:0, inner:1]
public class LabeledContinueInWhile {
    @org.yield4j.Generator public Iterable<String> method() {
		int i = 0;
		OUTER: while (i < 3) {
			yield_return("outer:" + i++);
			int j = 0;
			while (j < 5) {
				yield_return("inner:" + j++);
				if (j == 2)
					continue OUTER;
			}
		}
	}
}
