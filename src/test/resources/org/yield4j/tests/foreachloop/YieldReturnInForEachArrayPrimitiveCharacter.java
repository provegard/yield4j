package org.yield4j.tests.foreachloop;
import static org.yield4j.YieldSupport.*;

//>> [a, b, c]
public class YieldReturnInForEachArrayPrimitiveCharacter {
    @org.yield4j.Generator public Iterable<Character> method() {
		char[] chars = new char[] {'a', 'b', 'c'};
		for (char c : chars) {
			yield_return(c);
		}
	}
}
