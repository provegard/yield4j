package org.yield4j.java.astwrapper;

import java.util.List;

public class YieldBreakWrapper extends SingleExpressionStatementWrapper implements StatementWrapper {

    public YieldBreakWrapper(Object target, CallMethodWrapper expression) {
        super(target, expression);
    }

    @Override
    public WrapperType getType() {
        return WrapperType.YIELDBREAK;
    }

    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }

    public List<ExpressionWrapper> getArguments() {
        return ((CallMethodWrapper) getExpression()).getArguments();
    }
}
