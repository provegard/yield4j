package org.yield4j.java.astwrapper;

import java.util.List;

public class TryWrapper extends AbstractStatementWrapper {

    public TryWrapper(Object target,
            List<Wrapper> resources, BlockWrapper block,
            List<CatchWrapper> catches, FinallyWrapper finallyBlock) {
        super(target);
        put("resources", resources);
        put("block", block);
        put("finalizer", finallyBlock);
        put("catches", catches);
    }

    public TryWrapper(List<Wrapper> resources, BlockWrapper body,
            List<CatchWrapper> catches, FinallyWrapper finallyBlock) {
        this(null, resources, body, catches, finallyBlock);
    }

    @Override
    public WrapperType getType() {
        return WrapperType.TRY;
    }

    public BlockWrapper getBlock() {
        return get("block");
    }

    public FinallyWrapper getFinalizer() {
        return get("finalizer");
    }

    public List<CatchWrapper> getCatchers() {
        return get("catches");
    }
    
    public List<Wrapper> getResources() {
        return get("resources");
    }

    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }
}
