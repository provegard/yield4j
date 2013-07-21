package org.yield4j.tests.labelbreak;
import static org.yield4j.YieldSupport.*;

//>> [0, 1, 99]
public class LabeledBreakInSwitchInFor {
    @org.yield4j.Generator public Iterable<Integer> method() {
		LOOP: for (int i = 0; i < 5; i++) {
			yield_return(i);
			switch (i) {
			case 1:
				yield_return(99);
				break LOOP;
			}
		}
	}
}
