package org.yield4j.java;

import org.yield4j.java.astwrapper.Wrapper;

public interface CodeGenerator {

    void generateInPlace(Wrapper root);
    
    // For debugging purposes
    Object getResult();
}
