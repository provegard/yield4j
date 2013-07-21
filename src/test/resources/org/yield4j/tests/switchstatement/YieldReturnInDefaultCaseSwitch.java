package org.yield4j.tests.switchstatement;
import static org.yield4j.YieldSupport.*;

//>> [99]
public class YieldReturnInDefaultCaseSwitch {
    @org.yield4j.Generator public Iterable<Integer> method() {
		int i = 1;
		switch (i) {
		case 0:
			yield_return(1);
			break;
		default:
			yield_return(99);
			break;
		}
	}
}
