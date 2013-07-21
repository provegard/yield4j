package org.yield4j.builder.statement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.yield4j.GeneratorMethodContext;
import org.yield4j.java.astwrapper.BlockWrapper;
import org.yield4j.java.astwrapper.ExpressionWrapper;
import org.yield4j.java.astwrapper.IfWrapper;
import org.yield4j.java.astwrapper.StatementWrapper;
import org.yield4j.java.astwrapper.Wrapper;
import org.yield4j.java.astwrapper.WrapperType;

public class StatementPointers {

    private Iterable<StatementPointer> pointers;

    public StatementPointers(Iterable<StatementPointer> pointers) {
        this.pointers = pointers;
    }

    public void emit(List<StatementWrapper> statements,
            GeneratorMethodContext context) {

        if (statements.size() > 0) {
            Wrapper lastOne = statements.get(statements.size() - 1);
            if (lastOne.getType() == WrapperType.THROW) {
                // Don't emit anything after an interrupting statement!
                return;
            }
        }

        StatementPointer sp;
        Statement next = null;
        Iterator<StatementPointer> it = pointers.iterator();
        if (it.hasNext()) {
            // There are pointers, so get the first one.
            sp = it.next();
            next = sp.head;

            if (it.hasNext()) {
                // More than one pointer! Ignore the previous setting of "next"
                // and do some more stuff.
                List<StatementPointer> conditionals = new ArrayList<StatementPointer>();
                StatementPointer conditionalElse = null, unconditional = null;
                // The use of the iterator in this loop is a bit unorthodox, but
                // it's because we have already read/consumed the first element.
                int lastOne = 1;
                do {
                    Condition cond = sp.data;
                    if (cond != null) {
                        if (cond == ElseCondition.ELSE) {
                            assert conditionalElse == null : "There should be at most one conditional ELSE.";
                            conditionalElse = sp;
                        } else {
                            conditionals.add(sp);
                        }
                    } else {
                        assert unconditional == null : "There should be at most one unconditional.";
                        unconditional = sp;
                    }

                    // Guard for the last loop iteration...
                    if (it.hasNext()) {
                        sp = it.next();
                    }
                } while (it.hasNext() || lastOne-- > 0);

                assert !conditionals.isEmpty()
                        && (conditionalElse != null || unconditional != null) : "Unhandled statement heads.";

                // Create an if-else if-else if(-else) structure for now...
                StatementWrapper ifStatement = createElseIf(0, conditionals,
                        conditionalElse, context);
                statements.add(ifStatement);

                // If no else => execution resumes at the next, unconditional
                // statement.
                // Otherwise, we're done since the succeeding statement must be
                // an entry point, since it has multiple tails.
                next = conditionalElse == null ? unconditional.head : null;
            }
        }

        if (next != null) {
            next.emit(statements, context);
        }
    }

    private StatementWrapper createElseIf(int idx,
            List<StatementPointer> conditionals,
            StatementPointer conditionalElse, GeneratorMethodContext context) {
        // Get the pointer to process. A bit clumsy since one of the pointers
        // is outside of the list...
        StatementPointer ptr;
        if (idx < conditionals.size()) {
            ptr = conditionals.get(idx);
        } else if (conditionalElse != null) {
            ptr = conditionalElse;
        } else {
            // Nothing left to do, no else part!
            return null;
        }

        Condition cond = ptr.data;

        List<StatementWrapper> statements = new ArrayList<StatementWrapper>();
        ptr.head.emit(statements, context);

        StatementWrapper thenPart = statements.size() > 1 ? new BlockWrapper(
                statements) : statements.get(0);
        ExpressionWrapper expr = cond != null ? cond.makeExpression() : null;
        if (expr != null) {
            return new IfWrapper(expr, thenPart, createElseIf(idx + 1,
                    conditionals, conditionalElse, context));
        } else {
            return thenPart;
        }
    }
}
