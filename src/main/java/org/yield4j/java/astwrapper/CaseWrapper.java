package org.yield4j.java.astwrapper;

import java.util.List;

public class CaseWrapper extends AbstractWrapper {

    public CaseWrapper(Object target, ExpressionWrapper expression,
            List<StatementWrapper> statements) {
        super(target);
        put("expression", expression);
        put("statements", statements);
    }

    public CaseWrapper(ExpressionWrapper expression,
            List<StatementWrapper> statements) {
        this(null, expression, statements);
    }

    @Override
    public WrapperType getType() {
        return WrapperType.CASE;
    }

    public static CaseWrapper createDefaultCase(
            List<StatementWrapper> statements) {
        return new CaseWrapper(null, statements);
    }

    public List<StatementWrapper> getStatements() {
        return get("statements");
    }

    public ExpressionWrapper getExpression() {
        return get("expression");
    }

    @Override
    public void accept(WrapperVisitor v) {
        v.visit(this);
    }

}
