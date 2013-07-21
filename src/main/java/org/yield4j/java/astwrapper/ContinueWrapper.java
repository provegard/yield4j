package org.yield4j.java.astwrapper;

public class ContinueWrapper extends AbstractStatementWrapper {

    private final String label;

    public ContinueWrapper(Object target, String label) {
        super(target);
        this.label = label;
    }

    @Override
    public WrapperType getType() {
        return WrapperType.CONTINUE;
    }

    public String getLabel() {
        return label;
    }
    
    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }
}
