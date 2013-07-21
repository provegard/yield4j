package org.yield4j.builder.statement;

import static org.yield4j.builder.statement.GotoStatement.addPointerFrom;

import org.yield4j.GeneratorMethodContext;
import org.yield4j.java.astwrapper.BreakWrapper;
import org.yield4j.java.astwrapper.StatementWrapper;

public class GenericStatement extends Statement {

    public GenericStatement(StatementWrapper statement) {
        super(statement);
    }

    @Override
    public void transform(GeneratorMethodContext context,
            StatementPointer ptr) {
        // Action = break the switch we're in.
        StatementWrapper breakSwitch = new BreakWrapper(context.getMainSwitchLabel());

        // Add a pointer to a new statement that does
        // "goto entry point i" + break.
        addPointerFrom(this, ptr, breakSwitch);
    }
}
