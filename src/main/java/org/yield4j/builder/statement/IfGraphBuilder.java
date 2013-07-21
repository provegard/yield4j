package org.yield4j.builder.statement;

import java.util.List;
import java.util.Queue;

import org.yield4j.java.astwrapper.IfWrapper;
import org.yield4j.java.astwrapper.StatementWrapper;

public class IfGraphBuilder extends StatementGraphBuilder {

    public IfGraphBuilder(StatementWrapper ifStatement) {
        super(ifStatement);
    }

    @Override
    protected Statement build(Queue<StatementWrapper> statements,
            List<Statement> tails) {
        // Let the if statement point to its then and else parts.
        // If not else => point to then and successor.
        // Let the tails from the recursion point to the successor.
        IfWrapper ifStatement = (IfWrapper) statements.poll();
        Statement s = Statement.create(null).describeAs("if root");

        StatementWrapper thenStatement = ifStatement.getThenStatement();
        StatementWrapper elseStatement = ifStatement.getElseStatement();

        Graph g = new StatementGraphBuilder(thenStatement).build();
        Statement headThen = g.head;
        tails.addAll(g.tails);
        s.addHead(headThen, new ExpressionCondition(ifStatement.getCondition()));
        if (elseStatement != null) {
            g = new StatementGraphBuilder(elseStatement).build();
            Statement headElse = g.head;
            tails.addAll(g.tails);
            s.addHead(headElse, ElseCondition.ELSE);
        } else {
            // The if statement itself is also a tail, since execution will
            // continue after the if statement if its expression is not
            // satisfied.
            tails.add(s);
        }
        return s;
    }
}
