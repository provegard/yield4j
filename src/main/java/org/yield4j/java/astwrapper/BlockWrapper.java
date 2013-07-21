package org.yield4j.java.astwrapper;

import java.util.List;

public class BlockWrapper extends AbstractStatementWrapper {

    public BlockWrapper(Object target, List<StatementWrapper> statements) {
        super(target);
        put("statements", statements);
    }

    public BlockWrapper(List<StatementWrapper> statements) {
        this(null, statements);
    }

    @Override
    public WrapperType getType() {
        return WrapperType.BLOCK;
    }

    public List<StatementWrapper> getStatements() {
        return get("statements");
    }
    
    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }

}
