package org.yield4j.java.astwrapper;

import java.util.List;

public class ForWrapper extends AbstractStatementWrapper {

    public ForWrapper(Object target,
            List<StatementWrapper> init, ExpressionWrapper condition,
            List<StatementWrapper> update, StatementWrapper statement) {
        super(target);
        put("init", init);
        put("condition", condition);
        put("update", update);
        put("statement", statement);
    }

    @Override
    public WrapperType getType() {
        return WrapperType.FOR;
    }

    public List<StatementWrapper> getInitStatements() {
        return get("init");
    }

    public ExpressionWrapper getCondition() {
        return get("condition");
    }

    public List<StatementWrapper> getUpdateStatements() {
        return get("update");
    }

    public StatementWrapper getStatement() {
        return get("statement");
    }

    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }

}
