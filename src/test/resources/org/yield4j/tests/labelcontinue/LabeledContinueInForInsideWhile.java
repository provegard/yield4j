package org.yield4j.tests.labelcontinue;
import static org.yield4j.YieldSupport.*;

//>> [outer:0, inner:0, inner:1, outer:1, inner:0, inner:1, outer:2, inner:0, inner:1]
public class LabeledContinueInForInsideWhile {
    @org.yield4j.Generator public Iterable<String> method() {
		int i = 0;
		OUTER: while (i < 3) {
			yield_return("outer:" + i++);
			for (int j = 0; j < 5; j++) {
				yield_return("inner:" + j);
				if (j == 1)
					continue OUTER;
			}
		}
	}
}
