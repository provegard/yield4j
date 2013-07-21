package org.yield4j.tests.switchstatement;
import static org.yield4j.YieldSupport.*;

//>> [2, 3]
public class YieldReturnInFallThroughSwitch {
    @org.yield4j.Generator public Iterable<Integer> method() {
		int i = 1;
		switch (i) {
		case 0:
			yield_return(1);
		case 1:
			yield_return(2);
		case 2:
			yield_return(3);
		}
	}
}
