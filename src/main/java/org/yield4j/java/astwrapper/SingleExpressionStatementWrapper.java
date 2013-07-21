package org.yield4j.java.astwrapper;

public abstract class SingleExpressionStatementWrapper extends AbstractStatementWrapper {

    protected SingleExpressionStatementWrapper(Object target, ExpressionWrapper expression) {
        super(target);
        put("expression", expression);
    }
    
    public ExpressionWrapper getExpression() {
        return get("expression");
    }
}
