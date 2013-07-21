package org.yield4j.java.astwrapper;

public class CatchWrapper extends AbstractWrapper {

    public CatchWrapper(Object target, DeclareVariableWrapper parameter, BlockWrapper block) {
        super(target);
        put("parameter", parameter);
        put("block", block);
    }

    public CatchWrapper(DeclareVariableWrapper param, BlockWrapper block) {
        this(null, param, block);
        assert param.getFlags().isParameter() : "Variable of a catch must be a parameter.";
    }

    @Override
    public WrapperType getType() {
        return WrapperType.CATCH;
    }
    
    public DeclareVariableWrapper getParameter() {
        return get("parameter");
    }
    
    public BlockWrapper getBlock() {
        return get("block");
    }

    @Override
    public void accept(WrapperVisitor v) {
        v.visit(this);
    }
}
