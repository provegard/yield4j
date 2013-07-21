package org.yield4j.java.astwrapper;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractWrapper implements Wrapper {

    private Wrapper parent;
    private Object target;
    private Object reference;

    private Map<String, Object> children = new HashMap<String, Object>();
    private Scope scope;
    private boolean resolve;
    
    protected AbstractWrapper(Object target) {
        this.target = target;
    }
    
    @Override
    public void setNeedsLocalNameResolution(boolean needs) {
        resolve = needs;
    }
    
    @Override
    public boolean shouldResolveLocalName() {
        return resolve;
    }

    public Object getReference() {
        return reference;
    }

    public void setReference(Object reference) {
        this.reference = reference;
    }
    
    public void setScope(Scope s) {
        this.scope = s;
    }
    
    public Scope getScope() {
        return scope;
    }
    
    @Override
    public Wrapper getParent() {
        return parent;
    }
    
    @Override
    public void setParent(Wrapper parent) {
        this.parent = parent;
    }
    
    protected <T extends Wrapper> T own(T w) {
        if (w != null) {
            w.setParent(this);
        }
        return w;
    }
    
    protected <T extends Wrapper> List<T> own(List<T> wrappers) {
        if (wrappers == null) {
            return emptyList();
        }
        for (Wrapper w : wrappers) {
            w.setParent(this);
        }
        return wrappers;
    }

    @Override
    public Object getTarget() {
        return target;
    }
    
    @Override
    public void setTarget(Object target) {
        this.target = target;
    }

    @Override
    public WrapperType getType() {
        return WrapperType.OTHER;
    }
    
    protected void put(String key, Wrapper w) {
        children.put(key, own(w));
    }

    protected void put(String key, List<? extends Wrapper> ws) {
        children.put(key, own(ws));
    }

    protected <T extends Wrapper> void put(String key, T[] ws) {
        children.put(key, own(ws != null ? asList(ws) : null));
    }
    
    protected <T> T get(String key) {
        return (T) children.get(key);
    }

    @Override
    public void replace(Wrapper original, Wrapper repl) {
        if (original.getParent() != this) {
            throw new IllegalArgumentException("Replacement failed, original is not my child.");
        }
        boolean didReplace = false;
        for (Map.Entry<String, Object> entry : children.entrySet()) {
            if (entry.getValue() == original) {
                entry.setValue(repl);
                didReplace = true;
                break;
            } else if (entry.getValue() instanceof List && replaceInList((List) entry.getValue(), original, repl)) {
                didReplace = true;
                break;
            }
        }
        if (didReplace) {
            original.setParent(null);
            repl.setParent(this);
        }
    }

    private boolean replaceInList(List list, Wrapper original, Wrapper repl) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == original) {
                list.set(i, repl);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return target != null ? target.toString() : "<TBD:" + getClass().getSimpleName() + ">";
    }
}
