package org.yield4j.java.astwrapper;

public class DeclareVariableWrapper extends AbstractStatementWrapper {

    private final String name;
    private final Flags flags;

    public DeclareVariableWrapper(Object target, ExpressionWrapper type,
            ExpressionWrapper initExpression, Flags flags, String name) {
        super(target);
        this.name = name;
        put("type", type);
        put("init", initExpression);
        this.flags = flags;
    }

    //TODO: Order of args!
    public DeclareVariableWrapper(String name, ExpressionWrapper type,
            ExpressionWrapper initExpression, Flags flags) {
        this(null, type, initExpression, flags, name);
    }

    public DeclareVariableWrapper(String name, ExpressionWrapper type, Flags flags) {
        this(name, type, null, flags);
    }

    @Override
    public WrapperType getType() {
        return WrapperType.VARIABLE;
    }

    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }

    public String getName() {
        return name;
    }

    public ExpressionWrapper getVariableType() {
        return get("type");
    }

    public ExpressionWrapper getInit() {
        return get("init");
    }
    
    public Flags getFlags() {
        return flags;
    }

    public boolean isLocalVariable() {
        return getParent().getType() != WrapperType.CLASS;
    }
}
