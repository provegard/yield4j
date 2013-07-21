package org.yield4j.java;

import java.util.Set;

import org.yield4j.java.astwrapper.Wrapper;

public interface NameFinder {
    Set<String> findNames(Wrapper root);
}
