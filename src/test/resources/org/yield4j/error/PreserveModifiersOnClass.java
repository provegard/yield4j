package org.yield4j.error;
import static org.yield4j.YieldSupport.yield_return;

//!! ERROR(20) org.yield4j.error.PreserveModifiersOnClass.Inner has private access in org.yield4j.error.PreserveModifiersOnClass
public class PreserveModifiersOnClass {

    public Iterable<Integer> method() {
        return new Inner().method();
    }
    
    private static class Inner {
        @org.yield4j.Generator Iterable<Integer> method() {
            yield_return(5);
        }
    }
}

class OtherClass {
    int method() {
        return PreserveModifiersOnClass.Inner.method();
    }
}