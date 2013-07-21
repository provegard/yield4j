package org.yield4j.java.astwrapper;


public class AnyExpressionWrapper extends AbstractWrapper implements
        ExpressionWrapper {

    public AnyExpressionWrapper(Object target) {
        super(target);
    }

    @Override
    public void accept(WrapperVisitor v) {
        v.visit(this);
    }
}
