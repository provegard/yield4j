package org.yield4j.tests.foreachloop;
import static org.yield4j.YieldSupport.*;

//>> [aa, bb, cc]
public class YieldReturnInForEachArrayReferenceType {
    @org.yield4j.Generator public Iterable<String> method() {
		String[] strings = new String[] {"aa", "bb", "cc"};
		for (String s : strings) {
			yield_return(s);
		}
	}
}
