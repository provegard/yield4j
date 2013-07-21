package org.yield4j.builder.statement;

import java.util.List;

import org.yield4j.GeneratorMethodContext;
import org.yield4j.java.astwrapper.BreakWrapper;
import org.yield4j.java.astwrapper.StatementWrapper;

public class BreakStatement extends GenericStatement implements
        TargetingStatement {
    private final String label;

    public BreakStatement(BreakWrapper statement) {
        super(statement);
        label = statement.getLabel();
        describeAs("break" + (label != null ? " " + label : ""));
    }
    
    protected void emitThis(List<StatementWrapper> statements,
            GeneratorMethodContext context) {
        // Don't emit anything
    }

    @Override
    public boolean isBreak() {
        return true;
    }

    public String getLabel() {
        return label;
    }
}
