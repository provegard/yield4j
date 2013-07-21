package org.yield4j.java.astwrapper;


public interface Wrapper {
    Wrapper getParent();
    void setParent(Wrapper parent);
    Object getTarget();
    void accept(WrapperVisitor v);
    WrapperType getType();
    void replace(Wrapper original, Wrapper repl);
    void setScope(Scope s);
    Scope getScope();
    void setTarget(Object target);
    
    void setNeedsLocalNameResolution(boolean needs);
    boolean shouldResolveLocalName();

    void setReference(Object ref);
    Object getReference();
}
