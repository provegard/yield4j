package org.yield4j.tests.labelcontinue;
import static org.yield4j.YieldSupport.*;

//>> [outer:0, inner:0, inner:1, outer:1, inner:0, inner:1, outer:2, inner:0, inner:1]
public class LabeledContinueInDoWhile {
    @org.yield4j.Generator public Iterable<String> method() {
		int i = 0;
		OUTER: do {
			yield_return("outer:" + i++);
			int j = 0;
			do {
				yield_return("inner:" + j++);
				if (j == 2)
					continue OUTER;
			} while (j < 5);
		} while (i < 3);
	}
}
