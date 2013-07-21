package org.yield4j.java.astwrapper;

public class AnyStatementWrapper extends AbstractStatementWrapper {

    public AnyStatementWrapper(Object target) {
        super(target);
    }

    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }
}
