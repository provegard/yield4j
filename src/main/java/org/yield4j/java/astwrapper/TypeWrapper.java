package org.yield4j.java.astwrapper;

import static java.util.Collections.singletonList;


public class TypeWrapper extends AbstractWrapper implements ExpressionWrapper {

    private Class<?> typeClass;
    private String typeName;

    public TypeWrapper(String typeName) {
        super(null);
        this.typeName = typeName;
    }

    public TypeWrapper(Class<?> typeClass) {
        super(null);
        this.typeClass = typeClass;
    }

    public Class<?> getTypeClass() {
        return typeClass;
    }
    
    public String getTypeName() {
        return typeName;
    }

    public ExpressionWrapper makeGeneric(ExpressionWrapper typeParam) {
        return new GenericTypeWrapper(this, singletonList(typeParam));
    }
    
    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }
}
