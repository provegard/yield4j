package org.yield4j.java.astwrapper;

public class DoWrapper extends AbstractStatementWrapper {

    public DoWrapper(Object target, ExpressionWrapper condition, StatementWrapper statement) {
        super(target);
        put("condition", condition);
        put("statement", statement);
    }

    @Override
    public WrapperType getType() {
        return WrapperType.DO;
    }

    public ExpressionWrapper getCondition() {
        return get("condition");
    }

    public StatementWrapper getStatement() {
        return get("statement");
    }
    
    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }

}
