package org.yield4j.java.astwrapper;

public class ThrowWrapper extends SingleExpressionStatementWrapper {

    public ThrowWrapper(Object target, ExpressionWrapper expression) {
        super(target, expression);
    }

    public ThrowWrapper(ExpressionWrapper expression) {
        this(null, expression);
    }

    @Override
    public WrapperType getType() {
        return WrapperType.THROW;
    }

    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }

}
