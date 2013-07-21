package org.yield4j.java.astwrapper;

import static org.yield4j.java.astwrapper.GeneratedFlags.emptyFlags;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

public class MethodWrapper extends AbstractWrapper {

    private final String name;
    private final Flags flags;
    private final boolean overrides;
    private String localVariableSelector;
    private List<String> localVariableNames;

    public MethodWrapper(Object target, String name, BlockWrapper body,
            ExpressionWrapper returnType,
            List<DeclareVariableWrapper> parameters,
            List<ExpressionWrapper> throwExprs, Flags flags,
            boolean overrides) {
        super(target);
        this.name = name;
        put("body", body);
        put("parameters", parameters);
        put("returnType", returnType);
        put("throws", throwExprs);
        this.flags = flags;
        this.overrides = overrides;
    }

    public MethodWrapper(String name, BlockWrapper body,
            ExpressionWrapper returnType, List<DeclareVariableWrapper> params,
            Flags flags, boolean overrides, List<ExpressionWrapper> thrown) {
        this(null, name, body, returnType, params, thrown, flags, overrides);
    }
    
    public List<ExpressionWrapper> getThrows() {
        return get("throws");
    }

    public String getLocalVariableSelector() {
        return localVariableSelector;
    }

    public void setLocalVariableSelector(String localVariableSelector) {
        this.localVariableSelector = localVariableSelector;
    }

    public List<String> getLocalVariableNames() {
        return localVariableNames;
    }

    public void setLocalVariableNames(List<String> localVariableNames) {
        this.localVariableNames = localVariableNames;
    }

    public List<DeclareVariableWrapper> extractLocalVariables() {
        final List<DeclareVariableWrapper> vars = new ArrayList<DeclareVariableWrapper>();
        // The scope that the method belongs to is the class scope. Must start
        // with the scope of the body.
        getBody().getScope().accept(new ScopeVisitor() {
            @Override
            public void visit(Scope scope) {
                for (DeclareVariableWrapper v : scope.getVariables()) {
                    if (v.getFlags().isFinal()) {
                        // No action for now, leave this one local/final.
                    } else if (!v.getFlags().isParameter()) {
                        vars.add(new DeclareVariableWrapper(v.getName(), v
                                .getVariableType(), emptyFlags()));
                        ExpressionWrapper init = v.getInit();
                        StatementWrapper replacement;
                        if (init == null) {
                            replacement = new NoopWrapper();
                        } else {
                            // TODO: Replace with an assignment that has the 
                            // same source position.
                            NameWrapper name = new NameWrapper(v.getName());
                            name.setNeedsLocalNameResolution(true);
                            replacement = new AssignWrapper(name, v.getInit())
                                    .asStatement();
                        }
                        replacement.setReference(v.getTarget());
                        v.getParent().replace(v, replacement);
                    }
                }
            }
        });
        return vars;
    }

    @Override
    public WrapperType getType() {
        return WrapperType.METHOD;
    }

    public String getName() {
        return name;
    }

    public void setBody(BlockWrapper body) {
        put("body", body);
    }

    public BlockWrapper getBody() {
        return get("body");
    }

    public ExpressionWrapper getReturnType() {
        return get("returnType");
    }

    public List<DeclareVariableWrapper> getParameters() {
        return get("parameters");
    }

    public static Builder methodNamed(String name) {
        return new Builder(name);
    }

    @Override
    public void accept(WrapperVisitor v) {
        v.visit(this);
    }

    public boolean isOverride() {
        return overrides;
    }

    public Flags getFlags() {
        return flags;
    }

    public static class Builder {

        private String name;
        private ExpressionWrapper returnType;
        private List<DeclareVariableWrapper> params;
        private BlockWrapper body;
        private boolean overrides;
        private Flags flags;
        private List<ExpressionWrapper> thrown;

        Builder(String name) {
            this.name = name;
            thrown = new ArrayList<ExpressionWrapper>();
        }

        public Builder withReturnType(Class<?> retType) {
            return withReturnType(new TypeWrapper(retType));
        }

        public Builder withParameters(DeclareVariableWrapper... params) {
            this.params = asList(params);
            return this;
        }

        public Builder withBody(BlockWrapper body) {
            this.body = body;
            return this;
        }

        public MethodWrapper create() {
            Flags f = flags != null ? flags : emptyFlags();
            return new MethodWrapper(name, body, returnType, params, f,
                    overrides, thrown);
        }

        public Builder overrides() {
            overrides = true;
            return this;
        }

        public Builder withReturnType(ExpressionWrapper type) {
            returnType = type;
            return this;
        }

        public Builder withFlags(Flags flags) {
            this.flags = flags;
            return this;
        }

        public Builder withThrows(Class<? extends Throwable> throwsClass) {
            thrown.add(new TypeWrapper(throwsClass));
            return this;
        }
    }
}
