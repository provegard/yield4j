package org.yield4j.builder.statement;

import org.yield4j.java.astwrapper.StatementWrapper;

public class IterationStatementGraphBuilder extends StatementGraphBuilder {

    private String label;

    protected IterationStatementGraphBuilder(StatementWrapper statement, String label) {
        super(statement);
        this.label = label;
    }

    protected void processBodyTails(java.util.List<Statement> bodyTails,
            Statement exitPoint, Statement continuation,
            Condition continuationCondition, java.util.List<Statement> tails) {

        for (Statement tail : bodyTails) {
            boolean isTargetedAtMe = isTargetedAtMe(tail);
            boolean continueLoop = !tail.isBreak()
                    && (!tail.isContinue() || isTargetedAtMe);
            boolean escapeLoop = (tail.isContinue() && !isTargetedAtMe)
                    || (tail.isBreak() && !isTargetedAtMe);

            if (escapeLoop) {
                // The tail is destined for some outer loop, so we don't do
                // anything about it here.
                tails.add(tail);
            } else {
                if (continueLoop) {
                    // The loop should continue, so link the tail to the
                    // continuation statement.
                    tail.addHead(continuation, continuationCondition);
                }
                if (!continueLoop || continuationCondition != null) {
                    // The loop should exit, or we have added a conditional
                    // continuation before and need an unconditional exit.
                    tail.addHead(exitPoint, null);
                }
            }
        }
    }

    private boolean isTargetedAtMe(Statement s) {
        if (s instanceof TargetingStatement == false)
            return false;
        String targetLabel = ((TargetingStatement) s).getLabel();
        // A statement targeted at me has a null label when I have a null label,
        // or has my label.
        return targetLabel != null ? targetLabel.equals(label) : label == null;
    }
}
