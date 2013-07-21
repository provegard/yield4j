package org.yield4j.builder.statement;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.yield4j.java.astwrapper.CallMethodWrapper.callMethod;

import java.util.ArrayList;
import java.util.List;

import org.yield4j.GeneratorMethodContext;
import org.yield4j.builder.StateMachine;
import org.yield4j.builder.statement.StatementGraphBuilder.Graph;
import org.yield4j.java.astwrapper.BlockWrapper;
import org.yield4j.java.astwrapper.CatchWrapper;
import org.yield4j.java.astwrapper.DeclareVariableWrapper;
import org.yield4j.java.astwrapper.ExpressionWrapper;
import org.yield4j.java.astwrapper.FinallyWrapper;
import org.yield4j.java.astwrapper.IfWrapper;
import org.yield4j.java.astwrapper.StatementWrapper;
import org.yield4j.java.astwrapper.TryWrapper;
import org.yield4j.java.astwrapper.WrapperType;

public class TryStatement extends GenericStatement {

    public TryStatement(StatementWrapper tryStatement) {
        super(tryStatement);
    }

    @Override
    protected void emitThis(List<StatementWrapper> statements,
            GeneratorMethodContext context) {

        TryWrapper tryStatement = (TryWrapper) statement;

        StateMachine sm = new StateMachine();

        Graph graph = new StatementGraphBuilder(tryStatement.getBlock()).build();
        graph.verify(context);

        StatementWrapper newBody = sm.build(context, graph);
        BlockWrapper bodyBlock;
        if (newBody.getType() == WrapperType.BLOCK) {
            bodyBlock = (BlockWrapper) newBody;
        } else {
            bodyBlock = new BlockWrapper(asList(newBody));
        }

        FinallyWrapper newFinally = null;
        BlockWrapper finallyBlock = tryStatement.getFinalizer();
        if (finallyBlock != null) {
            ExpressionWrapper shouldRunFinally = callMethod(
                    context.getContextParameterName(), "shouldRunFinally")
                    .withoutArguments();

            newFinally = new FinallyWrapper(
                    singletonList((StatementWrapper) new IfWrapper(
                            shouldRunFinally, finallyBlock, null)));
        }

        List<CatchWrapper> catchers = tryStatement.getCatchers();
        List<CatchWrapper> newCatchers = new ArrayList<CatchWrapper>();
        for (CatchWrapper jc : catchers) {
            DeclareVariableWrapper param = jc.getParameter();
            BlockWrapper block = jc.getBlock();

            Graph cgraph = new StatementGraphBuilder(block).build();
            cgraph.verify(context);

            StatementWrapper catchBody = sm.build(context, cgraph);
            BlockWrapper catchBlock = new BlockWrapper(singletonList(catchBody));
            newCatchers.add(new CatchWrapper(param, catchBlock));
        }

        TryWrapper ts = new TryWrapper(null, bodyBlock, newCatchers, newFinally);
        statements.add(ts);
    }
}
