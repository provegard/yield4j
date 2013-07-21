package org.yield4j.tests.ifstatement;
import static org.yield4j.YieldSupport.*;

//>> [3, 0]
public class YieldReturnInIfElse {
    @org.yield4j.Generator public Iterable<Integer> method() {
		int i = 0;
		if (i < 0)
			yield_return(1);
		else if (i > 0)
			yield_return(2);
		else
			yield_return(3);
		yield_return(i);
	}

}
