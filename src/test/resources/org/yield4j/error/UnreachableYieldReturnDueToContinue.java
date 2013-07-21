package org.yield4j.error;
import static org.yield4j.YieldSupport.*;

//!! ERROR(9) unreachable code
public class UnreachableYieldReturnDueToContinue {
    @org.yield4j.Generator public Iterable<Integer> method() {
		for (int i = 0; i < 5; i++) {
			continue;
			yield_return(i);
		}
	}
}
