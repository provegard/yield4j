package org.yield4j.tests.sync;
import static org.yield4j.YieldSupport.yield_return;
import java.util.*;

//>> [200000]
public class YieldInSynchronizedMethod {
    private static final int ITER = 100000;
    private int i;
    private Object lock = new Object();
    
    public Iterable<Integer> method() throws InterruptedException {
        Runnable r = new Runnable() {
            public void run() {
                for (int i : it1()) {
                }
            }
        };
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        return Arrays.asList(i);
    }
    
    @org.yield4j.Generator private synchronized Iterable<Integer> it1() {
        for (int j = 0; j < ITER; j++) {
            yield_return(i++);
        }
    }
}
