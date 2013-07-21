package org.yield4j.java.astwrapper;


public class ReturnWrapper extends SingleExpressionStatementWrapper {

    public ReturnWrapper(Object target, ExpressionWrapper value) {
        super(target, value);
    }

    
    public ReturnWrapper(ExpressionWrapper value) {
        this(null, value);
    }

    public static ReturnWrapper returnFalse() {
        return new ReturnWrapper(new ValueWrapper(false));
    }
    
    public static ReturnWrapper returnTrue() {
        return new ReturnWrapper(new ValueWrapper(true));
    }

    @Override
    public WrapperType getType() {
        return WrapperType.RETURN;
    }

    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }
}
