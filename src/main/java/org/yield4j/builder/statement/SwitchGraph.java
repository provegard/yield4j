package org.yield4j.builder.statement;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.yield4j.java.astwrapper.CaseWrapper;
import org.yield4j.java.astwrapper.StatementWrapper;
import org.yield4j.java.astwrapper.SwitchWrapper;

public class SwitchGraph extends StatementGraphBuilder {

    private String label;

    public SwitchGraph(StatementWrapper switchStatement, String label) {
        super(switchStatement);
        this.label = label;
    }

    @Override
    protected Statement build(Queue<StatementWrapper> statements,
            List<Statement> tails) {
        SwitchWrapper switchStatement = (SwitchWrapper) statements.poll();

        Statement root = Statement.create(null).describeAs("switch root");

        // Create an exit point that we can point to from statements
        // that exit the switch statement.
        Statement exitPoint = Statement.create(null).describeAs("switch exit");
        tails.add(exitPoint);

        List<Statement> carryTails = new ArrayList<Statement>();
        for (CaseWrapper cjs : switchStatement.getCases()) {
            Graph g = new StatementGraphBuilder(cjs.getStatements()).build();
            Statement caseHead = g.head;

            // If there are any carry tails from the last case, point
            // them here before we continue.
            if (!carryTails.isEmpty()) {
                for (Statement ct : carryTails) {
                    ct.addHead(caseHead, null);
                }
                carryTails.clear();
            }

            java.util.List<Statement> caseTails = g.tails;
            if (caseTails.isEmpty()) {
                // Empty case, so use the previously created fake head
                // statement as the only tail.
                caseTails = singletonList(caseHead);
            }
            for (Statement tail : caseTails) {
                if (tail.isContinue()
                        || (tail.isBreak() && !isBreakForMe(tail))) {
                    // A continue cannot be aimed at the switch itself, so it
                    // must escape the switch statement.
                    tails.add(tail);
                } else if (tail.isBreak()) {
                    tail.addHead(exitPoint, null);
                } else {
                    // Neither break nor continue, so the tail should have an
                    // unconditional pointer to the head of the next case, if
                    // any. This is handled in the next iteration of the
                    // surrounding for loop.
                    carryTails.add(tail);
                }
            }

            Condition cond;
            if (cjs.getExpression() != null) {
                cond = new SwitchCondition(switchStatement.getExpression(),
                        cjs.getExpression());
            } else {
                cond = ElseCondition.ELSE;
            }

            root.addHead(caseHead, cond);
        }

        for (Statement ct : carryTails) {
            ct.addHead(exitPoint, null);
        }

        // Let the switch itself be a tail to create an unconditional pointer
        // to the succeeding statement. This is redundant if there is a default
        // case, in which case the pointer will be ignored later on anyway.
        // tails.add(root);
        root.addHead(exitPoint, null);

        return root;
    }

    private boolean isBreakForMe(Statement s) {
        if (!s.isBreak())
            return false;
        String breakLabel = ((BreakStatement) s).getLabel();
        // A break for me has a null label when I have a null label, or
        // has my label.
        return breakLabel != null ? breakLabel.equals(label) : label == null;
    }
}
