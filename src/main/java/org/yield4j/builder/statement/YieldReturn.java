package org.yield4j.builder.statement;

import static org.yield4j.builder.statement.GotoStatement.addPointerFrom;
import static org.yield4j.java.astwrapper.ReturnWrapper.returnTrue;

import java.util.List;

import org.yield4j.GeneratorMethodContext;
import org.yield4j.java.astwrapper.StatementWrapper;
import org.yield4j.java.astwrapper.Wrapper;
import org.yield4j.java.astwrapper.YieldReturnWrapper;

public class YieldReturn extends Statement {

    public YieldReturn(StatementWrapper statement) {
        super(statement);
    }

    @Override
    protected void emitThis(List<StatementWrapper> statements,
            GeneratorMethodContext context) {

        YieldReturnWrapper call = (YieldReturnWrapper) statement;

        Wrapper yieldValue = call.getArguments().get(0);
        statements.add(createStateAssignment(context, yieldValue));
    }

    @Override
    public void transform(GeneratorMethodContext context, StatementPointer ptr) {
        // Action = complete the step/return state.
        StatementWrapper returnTrue = returnTrue();

        // Add a pointer to a new statement that does
        // "goto entry point i" + return true.
        addPointerFrom(this, ptr, returnTrue);
    }
}
