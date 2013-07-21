package org.yield4j.tests.switchstatement;
import static org.yield4j.YieldSupport.*;

//>> [3]
public class YieldReturnInSwitch {
    @org.yield4j.Generator public Iterable<Integer> method() {
		int i = 2;
		switch (i) {
		case 0:
			yield_return(1);
			break;
		case 1:
			yield_return(2);
			break;
		case 2:
			yield_return(3);
			break;
		}
	}
}
