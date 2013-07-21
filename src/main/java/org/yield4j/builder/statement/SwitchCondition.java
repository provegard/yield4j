package org.yield4j.builder.statement;

import org.yield4j.java.astwrapper.EqualsWrapper;
import org.yield4j.java.astwrapper.ExpressionWrapper;

public class SwitchCondition implements Condition {
    private ExpressionWrapper lhs;
    private ExpressionWrapper rhs;

    public SwitchCondition(ExpressionWrapper lhs, ExpressionWrapper rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public ExpressionWrapper makeExpression() {
        return new EqualsWrapper(lhs, rhs);
    }

    @Override
    public String toString() {
        return lhs + " == " + rhs;
    }
}