package org.yield4j.builder.statement;

import java.util.List;
import java.util.Queue;

import org.yield4j.java.astwrapper.ForWrapper;
import org.yield4j.java.astwrapper.StatementWrapper;

public class ForGraphBuilder extends IterationStatementGraphBuilder {

    public ForGraphBuilder(StatementWrapper forLoop, String label) {
        super(forLoop, label);
    }

    @Override
    protected Statement build(Queue<StatementWrapper> statements,
            List<Statement> tails) {
        ForWrapper forStatement = (ForWrapper) statements.poll();

        List<StatementWrapper> initStatements = forStatement
                .getInitStatements();
        Condition condition = new ExpressionCondition(
                forStatement.getCondition());
        List<StatementWrapper> updateStatements = forStatement
                .getUpdateStatements();

        // Create fake statement to use as head for error reporting.
        Statement head = Statement.create(null).describeAs("for root");

        Graph g = new StatementGraphBuilder(forStatement.getStatement())
                .build();
        List<Statement> bodyTails = g.tails;
        Statement headBody = g.head;

        // Create sequence of init statements
        Statement[] inits = createSequence(initStatements, "init");

        head.addHead(inits[0], null);

        // Create an exit point that we can point to from statements
        // that exit the loop.
        Statement exitPoint = Statement.create(null).describeAs("for exit");
        tails.add(exitPoint);

        // Add a conditional pointer from the last init statement to the
        // first body statement.
        inits[1].addHead(headBody, condition);

        // If the condition is not met, go to the exit point.
        inits[1].addHead(exitPoint, null);

        // construct the sequence of update statements
        Statement[] updates = createSequence(updateStatements, "update");

        // Create a conditional pointer from the last update statement to
        // the first body statement.
        updates[1].addHead(headBody, condition);

        // If the condition is not met, go to the exit point.
        updates[1].addHead(exitPoint, null);

        processBodyTails(bodyTails, exitPoint, updates[0], null, tails);

        // The first init statement is our return value
        return head;
    }

    private Statement[] createSequence(List<StatementWrapper> statements,
            String type) {
        Graph g = new StatementGraphBuilder(statements).build();
        Statement head = g.head, last;
        assert g.tails.size() == 1 : "Number of tails == " + g.tails.size();
        last = g.tails.get(0);
        return new Statement[] { head, last };
    }
}
