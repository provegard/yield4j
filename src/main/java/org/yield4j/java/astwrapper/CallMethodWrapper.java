package org.yield4j.java.astwrapper;

import static java.util.Arrays.asList;

import java.util.List;

public class CallMethodWrapper extends AbstractWrapper implements
        ExpressionWrapper {

    public CallMethodWrapper(Object target, ExpressionWrapper selector,
            List<ExpressionWrapper> args) {
        super(target);
        put("selector", selector);
        put("args", args);
    }

    private CallMethodWrapper(String[] names, ExpressionWrapper... eargs) {
        this(null, new NameWrapper(names), asList(eargs));
    }

    public List<ExpressionWrapper> getArguments() {
        return get("args");
    }

    public ExpressionWrapper getMethodSelect() {
        return get("selector");
    }

    public static Builder callMethod(String... names) {
        return new Builder(names);
    }

    public static class Builder {
        private String[] names;

        Builder(String[] names) {
            this.names = names;
        }

        public CallMethodWrapper withArguments(Object... args) {
            ExpressionWrapper[] eargs = new ExpressionWrapper[args.length];
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof ExpressionWrapper) {
                    eargs[i] = (ExpressionWrapper) args[i];
                } else {
                    eargs[i] = new ValueWrapper(args[i]);
                }
            }
            return new CallMethodWrapper(names, eargs);
        }

        public CallMethodWrapper withoutArguments() {
            return new CallMethodWrapper(names);
        }

    }

    @Override
    public void accept(WrapperVisitor visitor) {
        visitor.visit(this);
    }

    public StatementWrapper asStatement() {
        return new ExpressionStatementWrapper(null, this);
    }

}
