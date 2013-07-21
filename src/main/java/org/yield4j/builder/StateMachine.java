package org.yield4j.builder;

import static org.yield4j.java.astwrapper.CallMethodWrapper.callMethod;
import static org.yield4j.java.astwrapper.CaseWrapper.createDefaultCase;
import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.List;

import org.yield4j.GeneratorMethodContext;
import org.yield4j.builder.statement.Statement;
import org.yield4j.builder.statement.StatementGraphBuilder;
import org.yield4j.builder.statement.StatementGraphBuilder.Graph;
import org.yield4j.builder.statement.StatementPointer;
import org.yield4j.debug.DebugUtil;
import org.yield4j.java.astwrapper.BreakWrapper;
import org.yield4j.java.astwrapper.CaseWrapper;
import org.yield4j.java.astwrapper.ExpressionWrapper;
import org.yield4j.java.astwrapper.LabeledWrapper;
import org.yield4j.java.astwrapper.StatementWrapper;
import org.yield4j.java.astwrapper.SwitchWrapper;
import org.yield4j.java.astwrapper.ValueWrapper;
import org.yield4j.java.astwrapper.WhileWrapper;

public class StateMachine {

    public StatementWrapper build(GeneratorMethodContext context, Graph g) {
        Statement head = g.head;

        int oldLevel = context.getCurrentLevel();
        context.setCurrentLevel(context.getNextFreeLevel());

        if (DebugUtil.shouldWriteDebugOutput()) {
            DebugUtil.writeStatementGraph("pre-transform-output.dot", head);
        }
        
        List<Statement> entryPoints = transformStatementGraph(context, g);

        if (DebugUtil.shouldWriteDebugOutput()) {
            DebugUtil.writeStatementGraph("post-transform-output.dot",
                    entryPoints.toArray(new Statement[entryPoints.size()]));
        }

        // Create a switch statement from all entry points!
        StatementWrapper sw = createSwitchStatement(context, entryPoints);

        // Enclose the switch statement in while (true) { ... } so that
        // execution can jump between entry points during the same step.
        StatementWrapper whileTrue = new WhileWrapper(new ValueWrapper(true),
                sw);

        if (DebugUtil.shouldWriteDebugOutput()) {
            Graph tempGraph = new StatementGraphBuilder(
                    singletonList(whileTrue)).build();
            DebugUtil.writeStatementGraph("final-output.dot", tempGraph.head);
        }

        StatementWrapper labelledWhile = new LabeledWrapper(
                context.getMainLoopLabel(), whileTrue);

        context.setCurrentLevel(oldLevel);
        return labelledWhile;
    }

    private StatementWrapper createSwitchStatement(
            GeneratorMethodContext context, List<Statement> entryPoints) {

        List<CaseWrapper> cases = new ArrayList<CaseWrapper>();
        for (int i = 0; i < entryPoints.size(); i++) {
            List<StatementWrapper> statements = new ArrayList<StatementWrapper>();
            entryPoints.get(i).emit(statements, context);

            CaseWrapper acase = new CaseWrapper(new ValueWrapper(i), statements);
            cases.add(acase);
        }

        // Add a default case where we break the main loop, to stop the
        // generation.
        StatementWrapper breakLoop = new BreakWrapper(
                context.getMainLoopLabel());
        CaseWrapper defCase = createDefaultCase(singletonList(breakLoop));
        cases.add(defCase);

        ExpressionWrapper getIndexCall = callMethod(
                context.getContextParameterName(), "getIndex").withArguments(
                context.getCurrentLevel());

        StatementWrapper sw = new SwitchWrapper(getIndexCall, cases);
        StatementWrapper labelledSwitch = new LabeledWrapper(
                context.getMainSwitchLabel(), sw);

        return labelledSwitch;
    }

    private List<Statement> transformStatementGraph(
            GeneratorMethodContext context, Graph g) {
        List<Statement> entryPoints = findEntryPoints(g.head);
        transformTails(g.tails, entryPoints, context);

        return entryPoints;
    }

    private void transformTails(List<Statement> tails,
            List<Statement> entryPoints, GeneratorMethodContext context) {
        // For each entry point, modify the tail(s) according to their yield
        // action
        // and detach the sub graph starting with the entry point.
        // No need to process the first one...
        for (int i = 1; i < entryPoints.size(); i++) {
            Statement ep = entryPoints.get(i);
            assert ep.incomingEdges().iterator().hasNext();
            for (StatementPointer ptr : ep.incomingEdges()) {
                ptr.tail.transform(context, ptr);
            }
            ep.detachFromTails();
        }

        // Process the outstanding tails also!
        for (Statement tail : tails) {
            tail.transform(context, null);
        }
    }

    private List<Statement> findEntryPoints(Statement head) {
        return new EntryPointFinder().find(head);
    }
}
