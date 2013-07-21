package org.yield4j.tests.forloop;
import static org.yield4j.YieldSupport.*;

//>> [0, 1, 2, 3, 4]
public class ForWithoutUpdate {
    @org.yield4j.Generator public Iterable<Integer> method() {
		for (int i = 0; i < 5;)
			yield_return(i++);
	}
}
