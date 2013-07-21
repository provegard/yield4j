package org.yield4j.java.astwrapper;


public class EqualsWrapper extends AbstractWrapper implements ExpressionWrapper {

    public EqualsWrapper(ExpressionWrapper lhs, ExpressionWrapper rhs) {
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
}
