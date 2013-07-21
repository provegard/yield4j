package org.yield4j.tests.arm;
import static org.yield4j.YieldSupport.*;
import java.util.*;

//@@ 1.[789]
//>> [begin, try, -ac2, -ac1]
public class VerifyExceptionHandlingWithYieldReturnInTryWithResources {
    private List<String> strings = new ArrayList<>();
    
    public Iterable<String> method() {
        try {
            for (String s : gen()) {
                strings.add(s);
            }
        } catch (Throwable t) {
            print(t, 0);
        }
        return strings;
    }

    @org.yield4j.Generator private Iterable<String> gen() {
        try (AC ac1 = new AC("ac1"); AC ac2 = new AC("ac2")) {
            yield_return("begin");
            if (true)
                throw new RuntimeException("try");
        }
    }
    
    private void print(Throwable t, int level) {
        String s = "";
        for (int i = 0; i < level; i++) {
            s = s + "-";
        }
        strings.add(s + t.getMessage());
        for (Throwable t2 : t.getSuppressed()) {
            print(t2, level + 1);
        }        
    }

    private class AC implements AutoCloseable {

        private String id;

        private AC(String id) {
            this.id = id;
        }

        @Override
        public void close() {
            throw new RuntimeException(id);
        }
    }
}
