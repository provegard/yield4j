package org.yield4j.error;
import static org.yield4j.YieldSupport.*;

//!! ERROR(10) undefined label: LABEL
public class UndefinedBreakLabelInTry {
    @org.yield4j.Generator public Iterable<Integer> method() {
        try {
            if (false)
                yield_break(); // force yield processing
            break LABEL;
        } finally {
        }
    }
}
