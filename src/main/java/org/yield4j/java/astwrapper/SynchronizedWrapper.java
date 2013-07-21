package org.yield4j.java.astwrapper;


public class SynchronizedWrapper extends AbstractStatementWrapper {

    public SynchronizedWrapper(Object target, ExpressionWrapper expression,
            BlockWrapper block) {
        super(target);
        put("expr", expression);
        put("block", block);
    }

    public ExpressionWrapper getExpression() {
        return get("expr");
    }
    
    public BlockWrapper getBlock() {
        return get("block");
    }
    
    @Override
    public void accept(WrapperVisitor v) {
        v.visit(this);
    }

}
