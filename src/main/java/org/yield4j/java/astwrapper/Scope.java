package org.yield4j.java.astwrapper;

import java.util.ArrayList;
import java.util.List;

public class Scope {

    private List<NamedWrapper> members;
    private List<Scope> subScopes = new ArrayList<Scope>();
    private List<Scope> childScopes = new ArrayList<Scope>();
    private Scope parent;

    public Scope(Scope parent) {
        this.members = new ArrayList<NamedWrapper>();
        this.parent = parent != null ? parent.getLatest() : null;
    }

    /**
     * Adds a local variable to this scope, effectively creating a new sub
     * scope. The parent of the new sub scope is the latest sub scope of the
     * current scope.
     * 
     * @param var
     *            the local variable to add
     */
    public void addLocalVariable(DeclareVariableWrapper var) {
        Scope s = new Scope(getLatest());
        s.insert(var, var.getName());
        subScopes.add(s);
    }

    /**
     * Inserts a named wrapper in this scope (this exact scope, not the latest
     * sub scope).
     * 
     * @param w
     *            the wrapper to insert
     * @param name
     *            the name of the wrapper
     */
    public void insert(Wrapper w, String name) {
        members.add(new NamedWrapper(name, w));
    }

    private Scope getLatest() {
        if (subScopes.size() == 0) {
            return this;
        }
        return subScopes.get(subScopes.size() - 1);
    }

    /**
     * Creates a new scope as a child scope of the latest sub scope of this
     * scope. The new scope has that scope as parent.
     * 
     * @return a new scope
     */
    public Scope newChild() {
        Scope latest = getLatest();
        Scope s;
        if (latest != this) {
            s = latest.newChild();
        } else {
            s = new Scope(this);
            childScopes.add(s);
        }
        return s;
    }

    public void accept(ScopeVisitor v) {
        v.visit(this);
        for (Scope s : subScopes) {
            s.accept(v);
        }
        for (Scope s : childScopes) {
            s.accept(v);
        }
    }

    /**
     * Returns a list of the variables that belong to this scope only. Variables
     * in child and sub scopes are not included.
     * 
     * @return a list of variables
     */
    public List<DeclareVariableWrapper> getVariables() {
        List<DeclareVariableWrapper> vars = new ArrayList<DeclareVariableWrapper>();
        for (NamedWrapper w : members) {
            if (w.wrapper.getType() == WrapperType.VARIABLE) {
                vars.add((DeclareVariableWrapper) w.wrapper);
            }
        }
        return vars;
    }

    /**
     * Determines if the given name refers to a variable that is visible in the
     * current scope. Parent scopes are searched backwards up until and
     * including the stop scope.
     * 
     * @param name
     *            the name to look for
     * @param stopScope
     *            the scope where to stop searching
     * @return whether the name is visible or not
     */
    public boolean isVisibleVariable(String name, Scope stopScope) {
        return getLatest().isVisibleVariable0(name, stopScope);
    }

    /**
     * Checks if the given name is in the current scope. Child and sub scopes are not searched.
     * 
     * @param name the name to check
     * @return whether the name is in the scope or not
     */
    public boolean isNameInScope(String name) {
        for (NamedWrapper nw : members) {
            if (nw.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean isVisibleVariable0(String name, Scope stopScope) {
        for (NamedWrapper nw : members) {
            if (nw.wrapper.getType() == WrapperType.VARIABLE
                    && nw.name.equals(name)) {
                return true;
            }
        }
        return parent != null && this != stopScope ? parent.isVisibleVariable0(
                name, stopScope) : false;
    }

    private class NamedWrapper {
        final String name;
        final Wrapper wrapper;

        NamedWrapper(String name, Wrapper wrapper) {
            this.name = name;
            this.wrapper = wrapper;
        }
    }
}
