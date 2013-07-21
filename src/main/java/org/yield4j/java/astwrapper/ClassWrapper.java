package org.yield4j.java.astwrapper;

import java.util.ArrayList;
import java.util.List;

public class ClassWrapper extends AbstractWrapper {

    private final String name;

    public ClassWrapper(Object target, String name, List<Wrapper> members) {
        super(target);
        put("members", members);
        this.name = name;
    }

    public ClassWrapper(String className, ExpressionWrapper extending) {
        super(null);
        put("extending", extending);
        this.name = className;
    }

    @Override
    public WrapperType getType() {
        return WrapperType.CLASS;
    }

    public List<MethodWrapper> getGeneratorMethods() {
        List<MethodWrapper> methods = new ArrayList<MethodWrapper>();
        for (Wrapper w : getMembers()) {
            if (w.getType() == WrapperType.METHOD) {
                methods.add((MethodWrapper) w);
            }
        }
        return methods;
    }

    public void addMembers(List<? extends Wrapper> newMembers) {
        List<Wrapper> members = getMembers();
        for (Wrapper w : newMembers) {
            members.add(own(w));
        }
    }

    public List<Wrapper> getMembers() {
        List<Wrapper> members = get("members");
        if (members == null) {
            members = new ArrayList<Wrapper>();
            put("members", members);
        }
        return members;
    }

    public static Builder anonymousClass() {
        return new Builder("");
    }

    public static Builder classNamed(String name) {
        return new Builder(name);
    }

    @Override
    public void accept(WrapperVisitor v) {
        v.visit(this);
    }

    public ExpressionWrapper getBaseType() {
        return get("extending");
    }

    public String getName() {
        return name;
    }

    public static class Builder {

        private String className;
        private ExpressionWrapper extending;

        Builder(String className) {
            this.className = className;
        }

        public Builder extending(ExpressionWrapper extending) {
            this.extending = extending;
            return this;
        }

        public ClassWrapper create() {
            return new ClassWrapper(className, extending);
        }
    }

    public boolean isNameOccupied(String name) {
        List<Wrapper> members = getMembers();
        if (members.size() == 0) {
            return false;
        }
        // Use the scope of the first member, because the scope of this wrapper
        // is the scope that the class is visible in.
        return members.get(0).getScope().isNameInScope(name);
    }
}
