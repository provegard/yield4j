package org.yield4j.java.astwrapper;

import java.util.List;

public class SwitchWrapper extends AbstractStatementWrapper {

    public SwitchWrapper(Object target, ExpressionWrapper expression, List<CaseWrapper> cases) {
        super(target);
        put("cases", cases);
        put("expression", expression);
    }

    public SwitchWrapper(ExpressionWrapper expression, List<CaseWrapper> cases) {
        this(null, expression, cases);
    }

    @Override
    public WrapperType getType() {
        return WrapperType.SWITCH;
    }

    public List<CaseWrapper> getCases() {
        return get("cases");
    }

    public ExpressionWrapper getExpression() {
        return get("expression");
    }

    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }

}
