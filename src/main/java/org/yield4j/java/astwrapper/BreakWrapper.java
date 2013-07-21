package org.yield4j.java.astwrapper;

public class BreakWrapper extends AbstractStatementWrapper {

    private final String label;

    public BreakWrapper(Object target, String label) {
        super(target);
        this.label = label;
    }

    public BreakWrapper(String label) {
        this(null, label);
    }

    @Override
    public WrapperType getType() {
        return WrapperType.BREAK;
    }

    public String getLabel() {
        return label;
    }
    
    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }
}
