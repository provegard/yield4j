package org.yield4j.builder.statement;

import java.util.List;

import org.yield4j.GeneratorMethodContext;
import org.yield4j.java.astwrapper.ContinueWrapper;
import org.yield4j.java.astwrapper.StatementWrapper;

public class ContinueStatement extends GenericStatement implements
        TargetingStatement {

    private String label;

    public ContinueStatement(ContinueWrapper statement) {
        super(statement);
        label = statement.getLabel();
        describeAs("continue" + (label != null ? " " + label : ""));
    }
    
    protected void emitThis(List<StatementWrapper> statements,
            GeneratorMethodContext context) {
        // Don't emit anything
    }

    @Override
    public boolean isContinue() {
        return true;
    }

    public String getLabel() {
        return label;
    }
}
