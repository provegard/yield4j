package org.yield4j.builder.statement;

import java.util.List;
import java.util.Queue;

import org.yield4j.java.astwrapper.ExpressionWrapper;
import org.yield4j.java.astwrapper.StatementWrapper;
import org.yield4j.java.astwrapper.WhileWrapper;

public class WhileGraphBuilder extends IterationStatementGraphBuilder {

    public WhileGraphBuilder(StatementWrapper whileLoop, String label) {
        super(whileLoop, label);
    }

    @Override
    protected Statement build(Queue<StatementWrapper> statements,
            List<Statement> tails) {
        WhileWrapper whileStatement = (WhileWrapper) statements.poll();
        Statement s = Statement.create(null).describeAs("while root");

        ExpressionWrapper condition = whileStatement.getCondition();
        Graph g = new StatementGraphBuilder(whileStatement.getStatement()).build();
        List<Statement> bodyTails = g.tails;
        Statement headBody = g.head;

        // Create an exit point that we can point to from statements
        // that exit the loop.
        Statement exitPoint = Statement.create(null).describeAs("while exit");
        tails.add(exitPoint);
        
        Condition c = new ExpressionCondition(condition);

        // Create a conditional pointer from the while statement to the first
        // body statement.
        s.addHead(headBody, c);

        // If the condition is not met, go to the exit point.
        s.addHead(exitPoint, null);

        processBodyTails(bodyTails, exitPoint, headBody, c, tails);

        return s;
    }
}
