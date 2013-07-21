package org.yield4j.builder.statement;

import org.yield4j.java.astwrapper.ExpressionWrapper;

public class ElseCondition implements Condition {

    public static final Condition ELSE = new ElseCondition();

    private ElseCondition() {
    }

    @Override
    public ExpressionWrapper makeExpression() {
        return null;
    }

    @Override
    public String toString() {
        return "ELSE";
    }
}
