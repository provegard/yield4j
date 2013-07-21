package org.yield4j.error;
import static org.yield4j.YieldSupport.*;

//!! ERROR(17) i has private access in org.yield4j.error.PreserveModifiersOnVariables
public class PreserveModifiersOnVariables {

    private int i = 5;
    
    @org.yield4j.Generator public Iterable<Integer> method() {
        yield_return(i);
    }

}

class OtherClass {
  void dummy() {
      new PreserveModifiersOnVariables().i = 4;
  }
}