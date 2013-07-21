package org.yield4j.builder.statement;

import java.util.List;

import org.yield4j.GeneratorMethodContext;
import org.yield4j.builder.StateMachine;
import org.yield4j.builder.statement.StatementGraphBuilder.Graph;
import org.yield4j.java.astwrapper.DeclareVariableWrapper;
import org.yield4j.java.astwrapper.StatementWrapper;

public class LocalFinalVariableStatement extends GenericStatement {

    private List<StatementWrapper> sourceStatements;

    public LocalFinalVariableStatement(List<StatementWrapper> statements) {
        super(null);
        this.sourceStatements = statements;
        describeAs("Local final variable scope, rooted at " + statements.get(0)
                + " - " + statements.size() + " statements in total.");
    }

    @Override
    protected void emitThis(List<StatementWrapper> statements,
            GeneratorMethodContext context) {
        DeclareVariableWrapper varStatement = (DeclareVariableWrapper) sourceStatements
                .get(0);

        StateMachine sm = new StateMachine();

        Graph graph = new StatementGraphBuilder(sourceStatements.subList(1,
                sourceStatements.size())).build();
        graph.verify(context);

        StatementWrapper rest = sm.build(context, graph);
        statements.add(varStatement);
        statements.add(rest);
    }

}
