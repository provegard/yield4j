package org.yield4j.java.astwrapper;

public class NameWrapper extends AbstractWrapper implements ExpressionWrapper {
    private final String[] parts;

    public NameWrapper(String...parts) {
        super(null);
        this.parts = parts;
    }

    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }

    public String[] getNameParts() {
        return parts;
    }
}
