package org.yield4j.java.astwrapper;

public class IfWrapper extends AbstractStatementWrapper {

    public IfWrapper(Object target,
            ExpressionWrapper condition, StatementWrapper thenStmt, StatementWrapper elseStmt) {
        super(target);
        put("then", thenStmt);
        put("else", elseStmt);
        put("condition", condition);
    }

    // TODO: sync order with other constructor
    public IfWrapper(ExpressionWrapper condition, StatementWrapper thenStmt, StatementWrapper elseStmt) {
        this(null, condition, thenStmt, elseStmt);
    }

    @Override
    public WrapperType getType() {
        return WrapperType.IF;
    }

    public StatementWrapper getThenStatement() {
        return get("then");
    }

    public StatementWrapper getElseStatement() {
        return get("else");
    }

    public ExpressionWrapper getCondition() {
        return get("condition");
    }

    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }

}
