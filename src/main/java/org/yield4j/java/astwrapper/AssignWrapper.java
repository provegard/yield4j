package org.yield4j.java.astwrapper;

public class AssignWrapper extends AbstractWrapper implements ExpressionWrapper {

    public AssignWrapper(ExpressionWrapper lhs, ExpressionWrapper rhs) {
        super(null);
        put("lhs", lhs);
        put("rhs", rhs);
    }

    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }

    public ExpressionWrapper getLeft() {
        return get("lhs");
    }

    public ExpressionWrapper getRight() {
        return get("rhs");
    }

    public StatementWrapper asStatement() {
        return new ExpressionStatementWrapper(null, this); //TODO: move to base class?
    }

}
