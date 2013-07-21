package org.yield4j.javac;

import java.util.HashSet;
import java.util.Set;

import org.yield4j.java.NameFinder;
import org.yield4j.java.astwrapper.Wrapper;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreeScanner;

public class JavacNameFinder implements NameFinder {

    @Override
    public Set<String> findNames(Wrapper wrapper) {
        Tree tree = (Tree) wrapper.getTarget();
        assert tree != null : "No tree for name finder to work on!";
        Set<String> names = new HashSet<String>();
        tree.accept(new Scanner(), names);
        return names;
    }

    private class Scanner extends TreeScanner<Void, Set<String>> {
    
        @Override
        public Void visitIdentifier(IdentifierTree arg0, Set<String> arg1) {
            arg1.add(arg0.getName().toString());
            return super.visitIdentifier(arg0, arg1);
        }
    }
}
