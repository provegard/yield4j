package org.yield4j.tests.ifstatement;
import static org.yield4j.YieldSupport.*;

//>> [2, 3]
public class YieldReturnInElse {
    @org.yield4j.Generator public Iterable<Integer> method() {
		int i = 1;
		if (i > 1)
			yield_return(1);
		else {
			yield_return(2);
			i++;
		}
		i++;
		yield_return(i);
	}
}
