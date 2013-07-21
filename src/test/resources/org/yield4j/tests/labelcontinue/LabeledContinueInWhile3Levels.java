package org.yield4j.tests.labelcontinue;
import static org.yield4j.YieldSupport.*;

//>> [outer:0, middle:0, inner:0, inner:1, outer:1, middle:0, inner:0, inner:1, outer:2, middle:0, inner:0, inner:1]
public class LabeledContinueInWhile3Levels {
    @org.yield4j.Generator public Iterable<String> method() {
		int i = 0;
		OUTER: while (i < 3) {
			yield_return("outer:" + i++);
			int j = 0;
			while (j < 2) {
				yield_return("middle:" + j++);
				int k = 0;
				while (k < 5) {					
					yield_return("inner:" + k++);
					if (k == 2)
						continue OUTER;
				}
			}
		}
	}
}
