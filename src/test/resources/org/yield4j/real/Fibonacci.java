package org.yield4j.real;
import static org.yield4j.YieldSupport.*;

//>> 16:[0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610]
public class Fibonacci {
    @org.yield4j.Generator public Iterable<Integer> fib() {
        yield_return(0);
        yield_return(1);
        int i1 = 0, i2 = 1;
        while (true) {
            int next = i1 + i2;
            yield_return(next);
            i1 = i2;
            i2 = next;
        }
    }
}
