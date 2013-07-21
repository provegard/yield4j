package org.yield4j.java.astwrapper;

public class NoopWrapper extends AbstractStatementWrapper {

    public NoopWrapper() {
        super(null);
    }

    @Override
    public void accept(WrapperVisitor v) {
        v.visit(this);
    }
}
