package org.yield4j.java.astwrapper;

import java.util.List;

public class YieldReturnWrapper extends SingleExpressionStatementWrapper implements
        StatementWrapper {

    public YieldReturnWrapper(Object target,
            CallMethodWrapper expression) {
        super(target, expression);
    }

    @Override
    public WrapperType getType() {
        return WrapperType.YIELDRETURN;
    }

    public List<ExpressionWrapper> getArguments() {
        return ((CallMethodWrapper) getExpression()).getArguments();
    }

    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }
}
