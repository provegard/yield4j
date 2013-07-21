package org.yield4j.javac;

import org.yield4j.java.IdentifierScanner;
import org.yield4j.java.astwrapper.Wrapper;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreeScanner;

public class JavacIdentScanner implements IdentifierScanner {

    @Override
    public void scan(Wrapper root, IdentifierAction action) {
        Tree tree = (Tree) root.getTarget();
        tree.accept(new IdentScanner(action), null);
    }

    private class IdentScanner extends TreeScanner<Void, Void> {

        private IdentifierAction action;
        
        IdentScanner(IdentifierAction action) {
            this.action = action;
        }
        
        @Override
        public Void visitIdentifier(IdentifierTree arg0, Void arg1) {
            String name = arg0.getName().toString();
            action.action(name);
            return super.visitIdentifier(arg0, arg1);
        }
        
    }
}
