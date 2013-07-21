package org.yield4j.javac.util;

import java.util.List;

import javax.tools.Diagnostic;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TryTree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;

public abstract class Ge7JavacUtils extends JavacUtils {

    private final JavacTrees trees;
    private final TreeMaker maker;

    protected Ge7JavacUtils(Context context) {
        trees = JavacTrees.instance(context);
        maker = TreeMaker.instance(context);
    }
    
    @Override
    public void printMessage(Diagnostic.Kind kind, CharSequence msg, Tree t,
            CompilationUnitTree root) {
        trees.printMessage(kind, msg, t, root);
    }
    
    @Override
    public JCTree.JCLiteral createLiteral(Object value) {
        if (value == null) {
            return maker.Literal(TypeTags.BOT, null);
        }
        return maker.Literal(value);
    }
    
    @Override
    public List<? extends Tree> getResources(TryTree tree) {
        return tree.getResources();
    }

}
