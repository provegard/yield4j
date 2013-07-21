package org.yield4j.tests.labelcontinue;
import static org.yield4j.YieldSupport.*;

//>> [for:0, switch:0, for:1, for:2, switch:2]
public class LabeledContinueInSwitchInFor {
    @org.yield4j.Generator public Iterable<String> method() {
		OUTER: for (int i = 0; i < 3; i++) {
			yield_return("for:" + i);
			switch (i) {
			case 1:
				continue OUTER;
			default:
				yield_return("switch:" + i);
			}
		}
	}
}
