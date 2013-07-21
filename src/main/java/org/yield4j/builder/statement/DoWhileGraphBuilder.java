package org.yield4j.builder.statement;

import java.util.List;
import java.util.Queue;

import org.yield4j.java.astwrapper.DoWrapper;
import org.yield4j.java.astwrapper.StatementWrapper;

public class DoWhileGraphBuilder extends IterationStatementGraphBuilder {

    public DoWhileGraphBuilder(StatementWrapper doWhile, String label) {
        super(doWhile, label);
    }

    @Override
    protected Statement build(Queue<StatementWrapper> statements,
            List<Statement> tails) {
        DoWrapper doStatement = (DoWrapper) statements.poll();
        Condition condition = new ExpressionCondition(doStatement.getCondition());
        
        // Create a fake statement for error reporting!
        Statement fakeHead = Statement.create(null).describeAs("do-while root");

        Graph g = new StatementGraphBuilder(doStatement.getStatement()).build();
        Statement head = g.head;
        java.util.List<Statement> bodyTails = g.tails;
        
        fakeHead.addHead(head, null);

        // Create an exit point that we can point to from statements
        // that exit the loop.
        Statement exitPoint = Statement.create(null).describeAs("do-while exit");
        tails.add(exitPoint);

        processBodyTails(bodyTails, exitPoint, head, condition, tails);

        return fakeHead;
    }
}
