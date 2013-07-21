package org.yield4j.tests.plain;
import static org.yield4j.YieldSupport.*;

//>> []
public class SingleYieldBreak {
    @org.yield4j.Generator public Iterable<String> method() {
		yield_break();
	}
}
