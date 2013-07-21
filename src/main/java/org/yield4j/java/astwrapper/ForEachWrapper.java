package org.yield4j.java.astwrapper;

public class ForEachWrapper extends AbstractWrapper {

    public ForEachWrapper(Object target, DeclareVariableWrapper variable,
            ExpressionWrapper expression, StatementWrapper statement) {
        super(target);
        put("init", variable);
        put("iterable", expression);
        put("body", statement);
    }

    public StatementWrapper getStatement() {
        return get("body");
    }

    public DeclareVariableWrapper getVariable() {
        return get("init");
    }

    public ExpressionWrapper getExpression() {
        return get("iterable");
    }

    @Override
    public void accept(WrapperVisitor v) {
        v.visit(this);
    }
}
