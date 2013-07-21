package org.yield4j.builder.statement;

import static org.yield4j.java.astwrapper.ReturnWrapper.returnFalse;

import java.util.List;

import org.yield4j.GeneratorMethodContext;
import org.yield4j.java.astwrapper.StatementWrapper;

public class YieldBreak extends Statement {

    public YieldBreak(StatementWrapper statement) {
        super(statement);
    }

    @Override
    public void transform(GeneratorMethodContext context,
            StatementPointer ptr) {
        // This statement can be a tail. No transformation needed though, since
        // we do return false in emitThis.
    }

    @Override
    protected void emitThis(List<StatementWrapper> statements, 
            GeneratorMethodContext context) {
        statements.add(returnFalse());
    }
}
