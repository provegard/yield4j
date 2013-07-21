package org.yield4j.tests.ifstatement;
import static org.yield4j.YieldSupport.*;

//>> [0, 1]
public class MultiStatementsInIf {
    @org.yield4j.Generator public Iterable<Integer> method() {
		int i = 0;
		if (i < 1) {
			yield_return(i);
			i++;
		}
		yield_return(i);
	}
}
