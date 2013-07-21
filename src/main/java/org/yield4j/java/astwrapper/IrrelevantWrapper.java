package org.yield4j.java.astwrapper;

public class IrrelevantWrapper extends AbstractWrapper {

    private final String name;

    public IrrelevantWrapper(Object target, String name) {
        super(target);
        this.name = name;
    }

    @Override
    public void accept(WrapperVisitor v) {
        v.visit(this);
    }

    public String getName() {
        return name;
    }

}
