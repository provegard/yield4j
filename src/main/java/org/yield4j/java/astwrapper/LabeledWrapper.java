package org.yield4j.java.astwrapper;

public class LabeledWrapper extends AbstractStatementWrapper {

    private final String label;

    public LabeledWrapper(Object target, StatementWrapper statement, String label) {
        super(target);
        this.label = label;
        put("statement", statement);
    }

    //TODO: Sync argument order
    public LabeledWrapper(String label, StatementWrapper statement) {
        this(null, statement, label);
    }

    @Override
    public WrapperType getType() {
        return WrapperType.LABELLED;
    }

    public String getLabel() {
        return label;
    }
    
    public StatementWrapper getStatement() {
        return get("statement");
    }

    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }

}
