package org.yield4j.java.astwrapper;

public class WhileWrapper extends AbstractStatementWrapper {

    public WhileWrapper(Object target, ExpressionWrapper condition, StatementWrapper statement) {
        super(target);
        put("condition", condition);
        put("statement", statement);
    }

    public WhileWrapper(ExpressionWrapper condition, StatementWrapper statement) {
        this(null, condition, statement);
    }

    @Override
    public WrapperType getType() {
        return WrapperType.WHILE;
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
