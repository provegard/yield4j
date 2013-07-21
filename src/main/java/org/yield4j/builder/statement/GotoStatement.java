package org.yield4j.builder.statement;

import java.util.List;

import org.yield4j.GeneratorMethodContext;
import org.yield4j.java.astwrapper.StatementWrapper;

public class GotoStatement extends Statement {

    private int gotoIndex;
    private StatementWrapper actionStatement;

    private GotoStatement(int gotoIndex, StatementWrapper actionStatement) {
        super(null);
        describeAs("goto " + gotoIndex);
        this.gotoIndex = gotoIndex;
        this.actionStatement = actionStatement;
    }

    @Override
    protected void emitThis(List<StatementWrapper> statements,
            GeneratorMethodContext context) {
        StatementWrapper assignIndex = createIndexAssignment(context, gotoIndex);
        statements.add(assignIndex);
        statements.add(actionStatement);
    }

    public static void addPointerFrom(Statement tail, StatementPointer ptr,
            StatementWrapper actionStatement) {
        int gotoIndex = ptr != null ? ptr.head.findIndex() : -1;
        Condition condition = ptr != null ? ptr.data : null;
        GotoStatement gotos = new GotoStatement(gotoIndex, actionStatement);
        tail.addHead(gotos, condition);
    }

    @Override
    public void transform(GeneratorMethodContext context, StatementPointer ptr) {
        throw new UnsupportedOperationException();
    }
}
