package org.yield4j.error;
import static org.yield4j.YieldSupport.*;

//!! ERROR(12) undefined label: LABEL
public class UndefinedBreakLabelInCatch {
    @org.yield4j.Generator public Iterable<Integer> method() {
        try {
            throw new NullPointerException();
        } catch (Exception ex) {
            if (false)
                yield_break(); // force yield processing
            break LABEL;
        }
    }
}
