package org.yield4j.error;
import static org.yield4j.YieldSupport.yield_return;

//!! ERROR(19) privateMethod() has private access in org.yield4j.error.PreserveModifiersOnMethods
public class PreserveModifiersOnMethods {

    public Iterable<Integer> method() {
        return privateMethod();
    }

    @org.yield4j.Generator private Iterable<Integer> privateMethod() {
        yield_return(5);
    }

}

class OtherClass {
    Iterable<Integer> dummy() {
        return new PreserveModifiersOnMethods().privateMethod();
    }
}