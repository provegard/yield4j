package org.yield4j.java.astwrapper;

public class ExpressionStatementWrapper extends SingleExpressionStatementWrapper {

    public ExpressionStatementWrapper(Object target, ExpressionWrapper expression) {
        super(target, expression);
    }
    
    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }
}