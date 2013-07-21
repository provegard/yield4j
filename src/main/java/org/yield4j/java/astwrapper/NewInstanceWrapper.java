package org.yield4j.java.astwrapper;

import static java.util.Arrays.asList;

import java.util.List;


public class NewInstanceWrapper extends AbstractWrapper implements ExpressionWrapper {

    public NewInstanceWrapper(Object target, ExpressionWrapper clazz, List<ExpressionWrapper> typeArgs, List<ExpressionWrapper> args,
            Wrapper impl) {
        super(target);
        put("class", clazz);
        put("typeArgs", typeArgs);
        put("args", args);
        put("impl", impl);
    }
    
    private NewInstanceWrapper(ExpressionWrapper clazz, ExpressionWrapper[] args,
            ClassWrapper impl) {
        this(null, clazz, null, args != null ? asList(args) : null, impl);
    }
    
    public ExpressionWrapper getClazz() {
        return get("class");
    }
    
    public List<ExpressionWrapper> getTypeArguments() {
        return get("typeArgs");
    }

    public List<ExpressionWrapper> getArguments() {
        return get("args");
    }

    public Wrapper getImplementation() {
        return get("impl");
    }

    public static Builder newInstanceOfClass(ExpressionWrapper clazz) {
        return new Builder(clazz);
    }

    public static class Builder {

        private final ExpressionWrapper clazz;
        private ExpressionWrapper[] args;
        private ClassWrapper impl;

        Builder(ExpressionWrapper clazz) {
            this.clazz = clazz;
        }

        public Builder withArguments(ExpressionWrapper... args) {
            this.args = args;
            return this;
        }

        public NewInstanceWrapper create() {
            return new NewInstanceWrapper(clazz, args, impl);
        }

        public Builder withImplementation(ClassWrapper impl) {
            this.impl = impl;
            return this;
        }
    }

    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }
}
