package org.yield4j.tests.forloop;
import static org.yield4j.YieldSupport.*;

//>> [0, 1, 2, 3, 4]
public class ForWithoutInit {
    @org.yield4j.Generator public Iterable<Integer> method() {
		int i = 0;
		for (; i < 5; i++)
			yield_return(i);
	}
}
