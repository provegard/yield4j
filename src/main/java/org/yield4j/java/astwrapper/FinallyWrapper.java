package org.yield4j.java.astwrapper;

import java.util.List;

public class FinallyWrapper extends BlockWrapper {

    public FinallyWrapper(Object target, List<StatementWrapper> statements) {
        super(target, statements);
    }

    public FinallyWrapper(List<StatementWrapper> statements) {
        super(statements);
    }
    
    @Override
    public WrapperType getType() {
        return WrapperType.FINALLY;
    }

    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }
}
