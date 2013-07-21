package org.yield4j.tests.switchstatement;
import static org.yield4j.YieldSupport.*;

//>> [2, 2, 3]
public class SwitchWithEmptyCase {
    @org.yield4j.Generator public Iterable<Integer> method() {
		for (int i = 0; i < 2; i++) {
			switch (i) {
			case 0:
			case 1:
				yield_return(2);
			}
		}
		yield_return(3);
	}
}
