package org.yield4j.java.astwrapper;

public class ValueWrapper extends AbstractWrapper implements ExpressionWrapper {
    private final Object value;

    public ValueWrapper(Object value) {
        super(null);
        this.value = value;
    }

    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }

    public Object getValue() {
        return value;
    }
}
