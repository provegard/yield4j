package org.yield4j.error;
import static org.yield4j.YieldSupport.*;

//!! ERROR(9) Cannot return a value from a generator. Use yield_return to return a value, or yield_break to end the generation.
public class ReturnInGenerator {
    @org.yield4j.Generator public Iterable<String> method() {
		if (false)
		    yield_break();
		return null;
	}
}
