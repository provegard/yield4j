package org.yield4j.java.astwrapper;

import java.util.List;

public class GenericTypeWrapper extends AbstractWrapper implements ExpressionWrapper {

    private final ExpressionWrapper outer;
    private final List<ExpressionWrapper> parameters;

    public GenericTypeWrapper(ExpressionWrapper outer, List<ExpressionWrapper> parameters) {
        this(null, outer, parameters);
    }

    public GenericTypeWrapper(Object target, ExpressionWrapper outer, List<ExpressionWrapper> parameters) {
        super(target);
        //TODO: own these!
        this.outer = outer;
        this.parameters = parameters;
    }

    public List<ExpressionWrapper> getTypeParameters() {
        return parameters;
    }
    
    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }

    public ExpressionWrapper getOuter() {
        return outer;
    }
}