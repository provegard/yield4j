package org.yield4j.java;

import org.yield4j.java.astwrapper.Wrapper;

public interface IdentifierScanner {
    void scan(Wrapper root, IdentifierAction action);
    
    public interface IdentifierAction {
        void action(String identifier);
    }
}
