package org.yield4j.java.astwrapper;

public class IsNotNullWrapper extends AbstractWrapper implements
        ExpressionWrapper {

    public IsNotNullWrapper(ExpressionWrapper expression) {
        super(null);
        put("expression", expression);
    }

    @Override
    public void accept(WrapperVisitor v) {
        v.visit(this);
    }

    public ExpressionWrapper getExpression() {
        return get("expression");
    }

}
