package org.yield4j.tests.trycatchfinally;
import static org.yield4j.YieldSupport.*;

import java.util.*;

//>> [2]
public class YieldBreakInTryWithFinally {
  int i = 0;
  
  public Iterable<Integer> method() {
      List<Integer> list = new ArrayList<Integer>();
      for (int x : helper()) {
          list.add(x);
      }
      list.add(i);
      return list;
  }
  
  @org.yield4j.Generator private Iterable<Integer> helper() {
      try {
          i++;
          yield_break();
      } finally {
          i++;
      }
  }

}
