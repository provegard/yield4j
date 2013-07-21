package org.yield4j.error;
import static org.yield4j.YieldSupport.yield_return;

//!! ERROR(10) yield_return cannot appear in a synchronized block
public class YieldReturnInSynchronized {
    private Object o = new Object();
    
    @org.yield4j.Generator public Iterable<String> method() {
        synchronized (o) {
            yield_return("nono");
        }
    }
}
