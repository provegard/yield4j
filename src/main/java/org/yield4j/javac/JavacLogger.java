package org.yield4j.javac;

import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

import org.yield4j.CompilationMessage;
import org.yield4j.CompilationMessage.MessageType;
import org.yield4j.java.Logger;
import org.yield4j.java.astwrapper.Wrapper;
import org.yield4j.javac.util.JavacUtilsImpl;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Log;

public class JavacLogger implements Logger {
    private final JavacTrees trees;
    private final JavacUtilsImpl jcutils;
    private final Log log;

    public JavacLogger(JavacProcessingEnvironment env) {
        Context ctx = env.getContext();
        jcutils = new JavacUtilsImpl(ctx);
        trees = JavacTrees.instance(ctx);
        log = Log.instance(ctx);
    }

    @Override
    public void printMessage(Element e, CompilationMessage msg) {
        Tree tree = (Tree) getMessageObject(msg.getWrapper());
        CompilationUnitTree cu = trees.getPath(e).getCompilationUnit();
        
        Kind kind = msg.getType() == MessageType.ERROR ? Kind.ERROR : Kind.WARNING;
        
        jcutils.printMessage(kind, msg.getMessage(), tree, cu);
    }

    @Override
    public void printNote(Element e, Wrapper wrapper, String note) {
        Tree tree = (Tree) getMessageObject(wrapper);
        CompilationUnitTree cu = trees.getPath(e).getCompilationUnit();
        jcutils.printMessage(Kind.NOTE, note, tree, cu);
    }

    @Override
    public void printNote(String note) {
        log.note("proc.messager", note);
    }
    
    private Object getMessageObject(Wrapper w) {
        Object ref = w.getReference();
        if (ref == null) {
            ref = w.getTarget();
        }
        if (ref == null) {
            throw new AssertionError("No message object found for " + w);
        }
        return ref;
    }
}
