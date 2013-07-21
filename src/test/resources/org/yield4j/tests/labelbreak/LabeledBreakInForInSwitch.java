package org.yield4j.tests.labelbreak;
import static org.yield4j.YieldSupport.*;

//>> [0, 99]
public class LabeledBreakInForInSwitch {
    @org.yield4j.Generator public Iterable<Integer> method() {
		int i = 1;
		SELECT: switch (i) {
		case 1:
			for (int j = 0; j < 5; j++) {
				yield_return(j);
				break SELECT;
			}
		}
		yield_return(99);
	}
}
