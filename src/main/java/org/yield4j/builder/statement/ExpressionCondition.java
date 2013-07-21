package org.yield4j.builder.statement;

import org.yield4j.java.astwrapper.ExpressionWrapper;

public class ExpressionCondition implements Condition {
    private ExpressionWrapper expression;

    public ExpressionCondition(ExpressionWrapper expr) {
        expression = expr;
    }

    @Override
    public ExpressionWrapper makeExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return expression.toString();
    }
}
