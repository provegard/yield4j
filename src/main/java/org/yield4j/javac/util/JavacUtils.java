package org.yield4j.javac.util;

import java.util.List;

import javax.tools.Diagnostic;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TryTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.util.Context;

public abstract class JavacUtils {
    public abstract JCExpression equalsExpression(JCExpression left, JCExpression right);

    public abstract JCExpression isNotNullExpression(JCExpression e);

    public abstract JCLiteral createLiteral(Object value);

    public abstract void printMessage(Diagnostic.Kind kind, CharSequence msg, Tree t,
            CompilationUnitTree root);
    
    public abstract List<? extends Tree> getResources(TryTree tree);

    /**
     * Retrieves a compile time constant of type int from the specified class
     * location. From Project Lombok (http://projectlombok.org/).
     * <p>
     * Solves the problem of compile time constant inlining, resulting in yield4j
     * having the wrong value (javac compiler changes private api constants from
     * time to time).
     *
     * @param ctcLocation location of the compile time constant
     * @param identifier the name of the field of the compile time constant.
     */
    public static int getCtcInt(Class<?> ctcLocation, String identifier) {
        try {
            return (Integer) ctcLocation.getField(identifier).get(null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static JavacUtils get(Context context) {
        return new JavacUtilsImpl(context);
    }
}
